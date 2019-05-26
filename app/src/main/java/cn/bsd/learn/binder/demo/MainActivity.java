package cn.bsd.learn.binder.demo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main_Activity";
    private IStudentManger mRemoteStudentManager;
    private int student_size = 2;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //获取到IStudentManager对象
            IStudentManger iStudentManger = IStudentManger.Stub.asInterface(service);
            mRemoteStudentManager = iStudentManger;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteStudentManager=null;
            Log.e(TAG,"onServiceDisconnected.threadname"+Thread.currentThread().getName());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,StudentManagerService.class);
        bindService(intent,mConnection,BIND_AUTO_CREATE);
    }


    public void toSecondActivity(View view) {
        Student.NAME = "LEARN";
        Log.d(TAG,Student.NAME);
        startActivity(new Intent(this,SecondActivity.class));
    }

    public void get_student_list(View view) {
        Toast.makeText(this,"正在获取学生列表",Toast.LENGTH_SHORT).show();
        //由于服务端的查询是耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(mRemoteStudentManager!=null){
                    try {
                        List<Student> studentList = mRemoteStudentManager.getStudentList();
                        student_size = studentList.size();
                        Log.e(TAG,"获取到的学生列表"+studentList.toString());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void addStudent(View view) {
        if(mRemoteStudentManager!=null){
            try {
                int student_id = student_size+1;
                Student newStudent = new Student(student_id,"测试"+student_id,student_id%2==0?"man":"woman");
                mRemoteStudentManager.addStudent(newStudent);
                Log.e(TAG,"添加一位学生列表"+newStudent.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
