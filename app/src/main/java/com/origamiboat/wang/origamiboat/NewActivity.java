package com.origamiboat.wang.origamiboat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.origamiboat.wang.origamiboat.common.ServerWebRoot;
import com.origamiboat.wang.origamiboat.data_storage.LoginService;
import com.origamiboat.wang.origamiboat.utils.CheckNet;
import com.origamiboat.wang.origamiboat.utils.richtext.RichEditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewActivity extends Activity {
    private static final String UPLOAD_URL = ServerWebRoot.getServerWebRoot() + "UploadArtical";
    private static String user_artical;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    EditText articleTitle;
    String titlestr = "";
    String date_str = "";
    String filerootdir = Environment.getExternalStorageDirectory() + "/Origamiboat/";
    Map<String, ?> map_new = null;
    private RichEditor mEditor;
    private TextView mPreview;
    private LoginService service_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        service_new = new LoginService(this);
        //提取用户数据
        map_new = service_new.getSharePreference("login");//提取用户数据
        if (map_new != null && !map_new.isEmpty()) {
            user_artical = map_new.get("username").toString();

        }

        articleTitle = (EditText) findViewById(R.id.articleTitle);
        mEditor = (RichEditor) findViewById(R.id.editor);
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.BLACK);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //    mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...");

        mPreview = (TextView) findViewById(R.id.preview);
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                mPreview.setText(text);
            }
        });

        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });


        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //mEditor.insertImage("file:///android_asset/ic3.png","dachshund");
                //权限申请
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                    } else {
                        gallery();
                    }
                } else {
                    gallery();
                }
            }
        });
        //完成按钮点击事件
        findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast.makeText(NewActivity.this, mPreview.getText(), Toast.LENGTH_LONG).show();
                if (!CheckNet.isNetaAvailable(NewActivity.this)) {
                    Toast.makeText(NewActivity.this, "请检查网络状态", Toast.LENGTH_LONG).show();
                    return;
                }
                titlestr = articleTitle.getText().toString();
                if (!articleTitle.getText().toString().trim().equals("")) {
                    String areastr = mPreview.getText().toString();
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
                    date_str = sDateFormat.format(new java.util.Date());
                    String done = replaceImgSrc(areastr);
                    //Toast.makeText(NewActivity.this, done, Toast.LENGTH_LONG).show();
                    String start = "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "<meta charset=\"utf-8\">\n" +
                            "\t<title></title>\n" +
                            "</head>\n" +
                            "<body>";
                    String end = "\n</body>\n" +
                            "</html>";
                    done = start + done + end;
                    //Toast.makeText(NewActivity.this,done, Toast.LENGTH_SHORT).show();
                    saveFile(done, user_artical + "_" + titlestr + "_" + date_str + ".html");
                    //上传html
                    uploadFile(filerootdir + user_artical + "_" + titlestr + "_" + date_str + ".html", 2);

                } else {
                    Toast.makeText(NewActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    /**
     * 工具栏添加图片的逻辑
     */
    public void gallery() {
        // 调用系统图库
        // Intent getImg = new Intent(Intent.ACTION_GET_CONTENT);
        // getImg.addCategory(Intent.CATEGORY_OPENABLE);
        // getImg.setType("image/*");
        Intent getImg = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(getImg, 1001);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {


                case 1001: {
                    Uri originalUri = data.getData();        //获得图片的uri
                    //bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);        //显得到bitmap图片
                    //这里开始的第二部分，获取图片的路径：
                    String[] proj = {MediaStore.Images.Media.DATA};
                    //好像是Android多媒体数据库的封装接口，具体的看Android文档
                    Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                    //按我个人理解 这个是获得用户选择的图片的索引值
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    //将光标移至开头 ，这个很重要，不小心很容易引起越界
                    cursor.moveToFirst();
                    //最后根据索引值获取图片路径
                    String path = cursor.getString(column_index);
                    //Toast.makeText(NewActivity.this, Environment.getExternalStorageDirectory() + path, Toast.LENGTH_LONG).show();
                    mEditor.insertImage(path, "dachshund");

                    //
                    // } catch (FileNotFoundException e) {
                    // // TODO Auto-generated catch block
                    // e.printStackTrace();
                    // Toast.makeText(this, e.getMessage(),
                    // Toast.LENGTH_LONG).show();
                    // }
                    break;
                }
                default:
                    break;
            }
        }
    }

    /**
     * 获取指定uri的本地绝对路径
     *
     * @param uri
     * @return
     */
    @SuppressWarnings("deprecation")
    protected String getAbsoluteImagePath(Uri uri) {
        // can post image
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public void uploadFile(String imgPathStr, final int mode) {
        final ProgressDialog waitdialog = new ProgressDialog(NewActivity.this);
        waitdialog.setTitle("等待");
        //waitdialog.setMessage("上传中...");
        //Toast.makeText(NewActivity.this,mode, Toast.LENGTH_LONG).show();
        if (mode == 2)
            waitdialog.setMessage("正在上传文字...");
        else {
            waitdialog.setMessage("正在上传图片...");
        }
        waitdialog.setIndeterminate(true);
        waitdialog.setCancelable(false);
        waitdialog.show();

        // 手机端要上传的文件的路径
        String filePath = imgPathStr;

        AsyncHttpClient httpClient = new AsyncHttpClient();

        RequestParams param = new RequestParams();
        try {
            param.put("file", new File(filePath));
            param.put("content", "wang");
            httpClient.post(UPLOAD_URL, param, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onSuccess(String arg0) {
                    super.onSuccess(arg0);
                    if (arg0.equals("success")) {
                        if (mode == 2) {
                            Toast.makeText(NewActivity.this, "上传成功！", Toast.LENGTH_LONG).show();
                            waitdialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable arg0, String arg1) {
                    super.onFailure(arg0, arg1);
                    Toast.makeText(NewActivity.this, "上传失败！", Toast.LENGTH_LONG).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(NewActivity.this, "上传文件不存在！", Toast.LENGTH_LONG).show();
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    //Toast.makeText(NewActivity.this,"权限申请成功", Toast.LENGTH_LONG).show();
                    gallery();
                } else {
                    // Permission Denied
                    Toast.makeText(NewActivity.this, "权限申请失败", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    private void showMessage(String message,
                             DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(NewActivity.this).setMessage(message)
                .setPositiveButton("OK", okListener).create().show();
    }

    /**
     * 替换内容中存在的img标签中的src值
     *
     * @param content
     * @return
     */
    public String replaceImgSrc(String content) {

        int k = 1;
        StringBuffer sb = new StringBuffer();
//目前img标签标示有3种表达式
//<img alt=""src="1.jpg"/> <img alt=""src="1.jpg"></img><img alt=""src="1.jpg">
//开始匹配content中的<img />标签
        Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher m_img = p_img.matcher(content);
        boolean result_img = m_img.find();
        if (result_img) {
            while (result_img) {
                StringBuffer sbSrc = new StringBuffer();
                //获取到匹配的<img />标签中的内容
                String str_img = m_img.group(2);
                //System.out.println(str_img);
                //开始匹配<img />标签中的src
                Pattern p_src = Pattern.compile("(src|SRC)=(\"|')(.*?)(\"|')");
                Matcher m_src = p_src.matcher(str_img);
                if (m_src.find()) {
                    //取得图片的src值
                    String str_src = m_src.group(3);
                    //复制图片的一个文件
                    copyFile(str_src, filerootdir + user_artical + "_" + titlestr + "_" + date_str + "_" + k + ".jpg");
                    //上传图片文件

                    uploadFile(filerootdir + user_artical + "_" + titlestr + "_" + date_str + "_" + k + ".jpg", 1);

                    //System.out.println(str_src);
                    //开始替换图片src
                    m_src.appendReplacement(sbSrc, "src=\"" + user_artical + "_" + titlestr + "_" + date_str + "_" + k + ".jpg\"");

                    m_src.appendTail(sbSrc);
                    m_img.appendReplacement(sb, "<img" + sbSrc.toString() + "/>");
                    k++;
                }

                //结束匹配<img />标签中的src
                //匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
                result_img = m_img.find();

            }
            m_img.appendTail(sb);
            return sb.toString();
        } else {
            return content;
        }


    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                //创建文件夹
                File cacheDir = new File(filerootdir);//设置目录参数
                cacheDir.mkdirs();//新建目录
                //复制图片
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[4096];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();

            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    //写文件
    public void saveFile(String str, String filename) {
        String filePath = null;

        filePath = filerootdir + filename;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }


           /* FileWriter fw = new FileWriter(filename);

            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"utf-8")));
            out.write(str);
            out.flush();
            out.close();*/

            FileOutputStream outStream = new FileOutputStream(file);

            outStream.write(str.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}