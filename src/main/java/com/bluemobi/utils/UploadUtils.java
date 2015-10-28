package com.bluemobi.utils;

import com.bluemobi.constant.Constant;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 七牛图片操作类
 * Created by tuyh on 2015/7/25.
 */
public class UploadUtils {

    public static void main(String[] args) throws QiniuException {
        Auth auth = Auth.create(Constant.ACCESS_KEY, Constant.SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth);

        File file = new File("E:/aa.jpg");
        Map<String, Object> map = upload(file).map();
        System.out.println("上传的图片路径：" + Constant.domain + map.get("key"));

        // 测试获取空间名列表
        //findBuckets(bucketManager);

        // 测试获取控件下的文件列表
        List<FileInfo> list = getFilesList(bucketManager, Constant.bucket);
        for (FileInfo fileInfo : list) {
            System.out.println(fileInfo.key);
        }
    }

    /**
     * 将二进制流格式的文件转换为File格式的文件
     * @param file
     * @return
     */
    public static File changeFile(MultipartFile file) {
        CommonsMultipartFile cf = (CommonsMultipartFile) file;
        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
        File files = fi.getStoreLocation();
        return files;
    }

    /**
     * 上传文件
     *
     * @param file   要上传的文件
     * @return
     */
    public static StringMap upload(File file) {
        UploadManager uploadManager = new UploadManager();
        Auth auth = Auth.create(Constant.ACCESS_KEY, Constant.SECRET_KEY);
        String token = auth.uploadToken(Constant.bucket, null, 3600, null, true);

        com.qiniu.http.Response r = null;

        try {
            r = uploadManager.put(file, null, token, null, null, false);
        } catch (QiniuException e) {
            e.printStackTrace();
        }

        StringMap map = null;
        try {
            map = r.jsonToMap();
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取空间名列表
     *
     * @param bucketManager 空间管理类
     * @return
     * @throws QiniuException
     */
    public static String[] findBuckets(BucketManager bucketManager) throws QiniuException {
        String[] buckets = bucketManager.buckets();
        return buckets;
    }

    /**
     * 批量获取文件列表
     *
     * @param bucketManager 空间管理类
     * @param bucket        空间名
     * @return
     */
    public static List<FileInfo> getFilesList(BucketManager bucketManager, String bucket) {
        BucketManager.FileListIterator it = bucketManager.createFileListIterator(bucket, "", 100, null);

        List<FileInfo> list = new ArrayList<FileInfo>();

        while (it.hasNext()) {
            FileInfo[] items = it.next();
            if (items.length >= 1) {
                list.add(items[0]);
            }
        }

        return list;
    }

    /**
     * 查看文件属性
     *
     * @param key      文件key（文件名）
     * @return
     */
    public static FileInfo getFile(String key) {
        Auth auth = Auth.create(Constant.ACCESS_KEY, Constant.SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth);
        FileInfo info = null;
        try {
            info = bucketManager.stat(Constant.bucket, key);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 复制文件
     *
     * @param targetBucket  空间名
     * @param key           文件key（文件名）
     * @param targetBucket  目标空间名
     * @param targetKey     目标文件key（目标文件名）
     */
    public static void copyFile(String bucket, String key, String targetBucket, String targetKey) {
        try {
            Auth auth = Auth.create(Constant.ACCESS_KEY, Constant.SECRET_KEY);
            BucketManager bucketManager = new BucketManager(auth);

            bucketManager.copy(bucket, key, targetBucket, targetKey);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重命名文件
     *
     * @param key           文件key（文件名）
     * @param newKey        新文件key（新文件名)
     */
    public static void renameFile(String key, String newKey) {
        try {
            Auth auth = Auth.create(Constant.ACCESS_KEY, Constant.SECRET_KEY);
            BucketManager bucketManager = new BucketManager(auth);

            bucketManager.rename(Constant.bucket, key, newKey);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }

    /**
     * 移动文件
     *
     * @param targetBucket  空间名
     * @param key           文件key（文件名）
     * @param targetBucket  目标空间名
     * @param targetKey     目标文件key（目标文件名）
     */
    public static void moveFile(String bucket, String key, String targetBucket, String targetKey) {
        try {
            Auth auth = Auth.create(Constant.ACCESS_KEY, Constant.SECRET_KEY);
            BucketManager bucketManager = new BucketManager(auth);

            bucketManager.move(bucket, key, targetBucket, targetKey);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param key           文件key（文件名）
     * @return
     */
    public static void deleteFile(String key) {
        try {
            Auth auth = Auth.create(Constant.ACCESS_KEY, Constant.SECRET_KEY);
            BucketManager bucketManager = new BucketManager(auth);
            String[] url = key.split("/");

            bucketManager.delete(Constant.bucket, url[3]);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }
}
