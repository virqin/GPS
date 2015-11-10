package com.example.gps;

import java.io.BufferedWriter;

import java.io.File;

import java.io.FileWriter;

import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.Calendar;

import java.util.Date;

import android.util.Log;

 

/**

 * ����־�ļ�����ģ��ֿɿؿ��ص���־����

 * 

 * @author Dsw

 * @version 1.0

 * @data 2012-2-20

 */

public class DswLog {

    private static char    LOG_TYPE='v';                            // ������־���ͣ�w����ֻ����澯��Ϣ�ȣ�v�������������Ϣ

    private static Boolean LOG_SWITCH = true;                       // ��־�ļ��ܿ���

    private static Boolean LOG_WRITE_TO_FILE = true;                // ��־д���ļ�����

     

    private static int  SDCARD_LOG_FILE_SAVE_DAYS = 0;              // sd������־�ļ�����ౣ������

 

    private static String LOGFILENAME = "gpsLog.txt";                  // �����������־�ļ�����

    private static String LOG_PATH_SDCARD_DIR = "/sdcard/";         // ��־�ļ���sdcard�е�·��

     

    private static SimpleDateFormat LogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   // ��־�������ʽ

    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");           // ��־�ļ���ʽ

 

    public static void w(String tag, String text) {

        log(tag, text, 'w');

    }

 

    public static void e(String tag, String text) {

        log(tag, text, 'e');

    }

 

    public static void d(String tag, String text) {

        log(tag, text, 'd');

    }

 

    public static void i(String tag, String text) {

        log(tag, text, 'i');

    }

 

    public static void v(String tag, String text) {

        log(tag, text, 'v');

    }

 

    /**

     * ����tag, msg�͵ȼ��������־

     * 

     * @param tag

     * @param msg

     * @param level

     * @return void

     * @since v 1.0

     */

    private static void log(String tag, String msg, char level) {

        if (LOG_SWITCH) {

            if ('i' == level) {

                Log.e(tag, msg);

            } else if ('e' == level) {

                Log.i(tag, msg);

            } else if ('w' == level) {

                Log.w(tag, msg);

            } else if ('d' == level) {

                Log.d(tag, msg);

            }else {

                Log.v(tag, msg);

            }

             

            if (LOG_WRITE_TO_FILE)

                writeLogtoFile(String.valueOf(level), tag, msg);

        }

    }

 

    /**

     * ����־�ļ���д����־

     * 

     * @return

     * **/

    private static void writeLogtoFile(String mylogtype, String tag, String text) {

        Date nowtime = new Date();

        String needWriteFiel = logfile.format(nowtime);

        String needWriteMessage = LogSdf.format(nowtime) + " " + mylogtype + " " + tag + " " + text;

         

        File file = new File(LOG_PATH_SDCARD_DIR, needWriteFiel + LOGFILENAME);

         

        try {

            FileWriter filerWriter = new FileWriter(file, true);//����������������ǲ���Ҫ�����ļ���ԭ�������ݣ������и���

            BufferedWriter bufWriter = new BufferedWriter(filerWriter);

            bufWriter.write(needWriteMessage);

            bufWriter.newLine();

            bufWriter.close();

            filerWriter.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

 

    /**

     * ɾ���ƶ�����־�ļ�

     * */

    public static void delFile() {

        String needDelFiel = logfile.format(getDateBefore());

        File file = new File(LOG_PATH_SDCARD_DIR, needDelFiel + LOGFILENAME);

        if (file.exists()) {

            file.delete();

        }

    }

 

    /**

     * �õ�����ʱ��ǰ�ļ������ڣ������õ���Ҫɾ������־�ļ���

     * */

    private static Date getDateBefore() {

        Date nowtime = new Date();

        Calendar now = Calendar.getInstance();

        now.setTime(nowtime);

        now.set(Calendar.DATE, now.get(Calendar.DATE) - SDCARD_LOG_FILE_SAVE_DAYS);

        return now.getTime();

    }

}