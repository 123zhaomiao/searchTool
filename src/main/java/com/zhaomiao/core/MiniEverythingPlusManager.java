package com.zhaomiao.core;

import com.zhaomiao.config.MiniEverythingPlusConfig;
import com.zhaomiao.core.dao.DataSourceFactory;
import com.zhaomiao.core.dao.FileIndexDao;
import com.zhaomiao.core.dao.impl.FileIndexDaoImpl;
import com.zhaomiao.core.index.FileScan;
import com.zhaomiao.core.index.impl.FileScanImpl;
import com.zhaomiao.core.interceptor.impl.FileIndexInterceptor;
import com.zhaomiao.core.interceptor.impl.ThingClearInterceptor;
import com.zhaomiao.core.model.Condition;
import com.zhaomiao.core.model.Thing;
import com.zhaomiao.core.monitor.FileWatch;
import com.zhaomiao.core.monitor.impl.FileWatchImpl;
import com.zhaomiao.core.search.FileSearch;
import com.zhaomiao.core.search.impl.FileSearchImpl;

import javax.sql.DataSource;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 统一调度器
 * 1) 构建索引
 * 2) 检索数据
 */
public  final class MiniEverythingPlusManager {

    private static volatile MiniEverythingPlusManager manager;

    private  FileScan fileScan ;
    private  FileSearch fileSearch;
    private ExecutorService executorService = null;

    private ThingClearInterceptor thingClearInterceptor;
    private  Thread backgroundClearThread;
    private AtomicBoolean backgroundClearThreadStatues =new AtomicBoolean(false);

    private MiniEverythingPlusManager(){
        this.initComponent();
    }
    /**
     * 文件监控
     */
    private FileWatch fileWatch;
    /**
     * 初始化数据库
     * 第一次使用的时候、重建索引的时候初始化
     */
    public void initOrResetDatabase(){
        DataSourceFactory.initDatabase();
    }

    private void initComponent(){
        //1.获取数据源
        DataSource dataSource = DataSourceFactory.dataSource();
        //2.初始化数据库
        initOrResetDatabase();
        //3.DAO对象 数据的增删查改
        FileIndexDao fileIndexDao =  new FileIndexDaoImpl(dataSource);
        this.fileSearch = new FileSearchImpl(fileIndexDao);
        this.fileScan = new FileScanImpl();
        this.fileScan.interceptor(new FileIndexInterceptor(fileIndexDao));

        this.thingClearInterceptor = new ThingClearInterceptor(fileIndexDao);

        this.backgroundClearThread = new Thread(this.backgroundClearThread);
        //将清理线程设置为守护线程
        this.backgroundClearThread.setDaemon(true);

        this.fileWatch = new FileWatchImpl(fileIndexDao);
    }

    public static MiniEverythingPlusManager getInstance(){
        if(manager == null){
            synchronized (MiniEverythingPlusManager.class){
                if(manager == null){
                    manager = new MiniEverythingPlusManager();
                }
            }
        }
        return manager;
    }
    /**
     * 检索
     */
    public  List<Thing> search(Condition condition) {
        //当检测到本地文件被删除时不输出
        //Stream JDK1.8流式处理
        return this.fileSearch.search(condition).stream().filter(
                thing -> {
                    String path = thing.getPath();
                    File f = new File(path);
                    boolean flag = f.exists();
                    if (!flag) {
                        //删除
                        thingClearInterceptor.apply(thing);
                    }
                    return flag;
                }).collect(Collectors.toList());
    }

    /**
     * 索引
     */
    public void buildIndex(){
        //重建或者初始化数据库
        initOrResetDatabase();
        //1.获取能被遍历的磁盘路径
        Set<String> set =
                MiniEverythingPlusConfig.config().getIncoudePath();
        //2.初始化线程池
        if(this.executorService == null){
            //新建一个固定大小的线程池
            executorService = Executors.newFixedThreadPool(set.size(), new ThreadFactory() {
                private final AtomicInteger threadID
                        = new AtomicInteger(0);
                //为线程起名字
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("Thread - scan"+threadID.getAndIncrement());
                    return thread;
                }
            });
        }

        final CountDownLatch countDownLatch = new CountDownLatch(set.size());
        System.out.println("Build index start...");
        System.out.println("please wait...");

        //3.创建多线程 每个线程遍历一个磁盘
        for(String path:set){
            this.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    MiniEverythingPlusManager.this.fileScan.index(path);
                    //任务完成 值-1
                    countDownLatch.countDown();
                }
            });
        }
        try {
            /**
             * 阻塞 直到任务完成 值==0
             */
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Build index end...");
    }

    /**
     * 启动后台清理线程
     */
    public void startBackgroundClearThread(){
        //执行CAS操作
        if(this.backgroundClearThreadStatues.compareAndSet(false,true)){
            this.backgroundClearThread.start();
        }else{
            System.out.println("不能重复启动");
        }
    }
    /**
     * 启动文件系统监听
     */
    public void startFileSystemMonitor(){
        this.fileWatch.monitor();
        //启用一个线程去监听
       new Thread(new Runnable() {
           @Override
           public void run() {
               fileWatch.start();
           }
       }).start();
    }
}
