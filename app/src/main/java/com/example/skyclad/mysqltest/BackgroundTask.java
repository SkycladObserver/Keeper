package com.example.skyclad.mysqltest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by acer on 10/24/2016.
 */

public class BackgroundTask extends AsyncTask<String,Void,String> {
    int userID;
    public static final String DEFAULT = "N/A";
    String fname,lname,uname,pass,email;
    String jsonString;
    String jsonData;
    String method;
    SharedPreferences sharedPreferences;
    AlertDialog alertDialog;
    Context ctx;
    View rootView;
    public BackgroundTask(Context ctx){
        this.ctx = ctx;
        sharedPreferences = ctx.getSharedPreferences("UserData",ctx.MODE_PRIVATE);
    }
    public BackgroundTask(Context ctx,View rootView){
        super();
        this.rootView = rootView;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Login Information");
    }

    @Override
    protected String doInBackground(String... params) {
        //10.101.9.90
        //10.0.2.2
        //String reg_url = "http://10.101.9.90/webapp/register.php";
        //String login_url = "http://10.101.9.90/webapp/login.php";
        String add_item_url = "http://skycladobserver.net23.net/add_item.php";
        String json_url = "http://skycladobserver.net23.net/json_get_data.php";
        String reg_url = "http://skycladobserver.net23.net/register.php";
        String login_url = "http://skycladobserver.net23.net/login.php";
        method = params[0];
        if(method.equals("register")) {
            String fname = params[1];
            String lname = params[2];
            String uname = params[3];
            String pass = params[4];
            String email = params[5];
            Log.d("Before try", "Before try");
            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("fname","UTF-8") + "=" +URLEncoder.encode(fname,"UTF-8") + "&" +
                        URLEncoder.encode("lname","UTF-8") + "=" +URLEncoder.encode(lname,"UTF-8") + "&" +
                        URLEncoder.encode("uname","UTF-8") + "=" +URLEncoder.encode(uname,"UTF-8") + "&" +
                        URLEncoder.encode("pass","UTF-8") + "=" + URLEncoder.encode(pass,"UTF-8") + "&" +
                        URLEncoder.encode("email","UTF-8") + "=" +URLEncoder.encode(email,"UTF-8");
                bw.write(data);
                bw.flush();
                bw.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                httpURLConnection.disconnect();
                Log.d("Registration Successful", "Registration Successful");
                return "Registration Successful";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(method.equals("login")){
            String name = params[1];
            String pass = params[2];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data = URLEncoder.encode("name","UTF-8") + "=" +URLEncoder.encode(name,"UTF-8") + "&" +
                        URLEncoder.encode("pass","UTF-8") + "=" + URLEncoder.encode(pass,"UTF-8");
                bw.write(data);
                bw.flush();
                bw.close();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                Log.d("login","IS instantiate");
                StringBuilder sb = new StringBuilder();
                String jsonString = "";
                while((jsonString = br.readLine())!=null){
                    sb.append(jsonString+"\n");
                }
                Log.d("login",sb.toString());
                br.close();
                is.close();
                httpURLConnection.disconnect();
                jsonString = sb.toString().trim();//.substring(sb.indexOf("{"));
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                JSONObject JO = jsonArray.getJSONObject(0);
                this.userID = JO.getInt("userID");
                this.fname = JO.getString("fname");
                this.lname = JO.getString("lname");
                this.uname = JO.getString("uname");
                this.pass = JO.getString("pass");
                this.email = JO.getString("email");
                Log.d("login",this.userID+" "+this.fname+" "+this.lname+" "+this.uname+" "+this.pass+" "+this.email);
                Log.d("login",jsonString);
                return jsonString;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
                return "{}";
            }
        }else if(method.equals("addItem")){
            String name = params[1];
            String description = params[2];
            String location = params[3];
            String time = params[4];
            String type = params[5];
            String userID = Integer.toString(sharedPreferences.getInt("userID",0));
            Log.d("addItem", "Before try");
            try {
                URL url = new URL(add_item_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8") + "&" +
                        URLEncoder.encode("location", "UTF-8") + "=" + URLEncoder.encode(location, "UTF-8") + "&" +
                        URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8") + "&" +
                        URLEncoder.encode("claimed", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8") + "&" +
                        URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&" +
                        URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(userID, "UTF-8");
                Log.d("addItem",data);
                bw.write(data);
                bw.flush();
                bw.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                httpURLConnection.disconnect();
                Log.d("addItem", "Add Successful");
                return "Add Successful";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch(ConnectException e){
                Toast.makeText(ctx, "Was not able to add item due to connection problems. Please try again.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(method.equals("getJson")){
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String jsonString = "";
                while((jsonString = bufferedReader.readLine())!=null){
                    sb.append(jsonString+"\n");
                }
                bufferedReader.close();
                is.close();
                httpURLConnection.disconnect();
                return sb.toString().trim();
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if (method.equals("register"))
            Toast.makeText(ctx,result, Toast.LENGTH_LONG).show();
        else if (method.equals("login")) {
            if(result.equals("{}")){
                alertDialog.setMessage("Login Failed. Please try again.");
                alertDialog.show();
            }else{
                SharedPreferences sharedPreferences = ctx.getSharedPreferences("UserData",ctx.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("userID",userID);
                editor.putString("fname",fname);
                editor.putString("uname",lname);
                editor.putString("uname",uname);
                editor.putString("pass",pass);
                editor.putString("email",email);
                editor.commit();
                ctx.startActivity(new Intent(ctx,ViewPagerActivity.class));
                ((Activity)ctx).finish();
            }
        }else if (method.equals("getJson")){
            TextView tv = (TextView) rootView;
            tv.setText(result);
        }
    }
}