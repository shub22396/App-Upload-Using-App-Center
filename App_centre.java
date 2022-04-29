

import javafx.fxml.Initializable;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;




public class App_centre {

    public static Integer a;
    public static  String name;
    public  static  String oname;
    //Initialize Appcentre API Token for use in below https request
    public static String api_token="b831bcd88b4466262f2e3d663fa8c7ae7787833b";



    public static void main(String[] args) throws Exception {

//----------Request to get "owner:name" and "name" of the APP on the app center--------------------------------------------------
            OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
            Request request = new Request.Builder()
                .url("https://api.appcenter.ms/v0.1/apps")
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                // Api Token need to pass as an parameter
                .addHeader("X-Api-Token", api_token)
                .build();
    //Capturing the response of the above request
            Response response = client.newCall(request).execute();
            String Responsebody= response.body().string();



            JSONArray jsonarray = new JSONArray(Responsebody);
            for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
        // Accessing App name and owner name from the object----------
            name = jsonobject.getString("name");
            JSONObject owner = jsonobject.getJSONObject("owner");
            oname = owner.getString("name");
            // System.out.println("name of the owner"+oname);
        }




//--------------------------------Request2 to get id of the APP on the app center-----------------------------


            OkHttpClient client2 = new OkHttpClient().newBuilder().build();
            String url2 = "https://api.appcenter.ms/v0.1/apps/" + oname + "/" + name + "/releases";
            //System.out.println("---" + url2);
            Request request2 = new Request.Builder().url(url2)

                    .method("GET", null)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-Api-Token", api_token)
                    .build();

            Response response2 = client2.newCall(request2).execute();

    // fetching response of the above request
        String responsebody2 = response2.body().string();

            //System.out.println("================="+responsebody2);

        JSONArray op = new JSONArray(responsebody2);

        for (int j = 0; j < op.length(); j++) {
                JSONObject op2 = op.getJSONObject(j);
                //System.out.println("OPPO"+op2);
            //---------accessing id of the app from the response object-----------------
                a = op2.getInt("id");


            }


//----------------Request to get app "download url" that needs to be passed in the forth request--------------------
        OkHttpClient client3 = new OkHttpClient().newBuilder()
                    .build();
//---------------Need to pass oname , name and id-----------------------------
        String url3= "https://api.appcenter.ms/v0.1/apps/"+oname+"/"+name+"/releases/"+a;

            System.out.println(url3);
            Request request3 = new Request.Builder()
                    .url(url3)
                    .method("GET", null)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-Api-Token", api_token)
                    .build();
            Response response3 = client3.newCall(request3).execute();

            String responsebody3= response3.body().string();
            //System.out.println("responsebody3"+responsebody3);

            JSONObject bc= new JSONObject(responsebody3);
            //Storing app url in a variable
            String app_url= bc.getString("download_url");



//---------------------Request to upload an app of lambdatest by passing app url and app name------------------
        OkHttpClient client4 = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("url",app_url)
                .addFormDataPart("name",name)
                .build();
        Request request4 = new Request.Builder()
                .url("https://manual-api.lambdatest.com/app/upload/realDevice")
                .method("POST", body)
                //Authorize with basic auth that can be get using LT_USERNAME and LT_ACCESS_KEY
                .addHeader("Authorization", "Basic <--Authorization--token->")
                .build();
        Response response4 = client4.newCall(request4).execute();

        String responsebody4 = response4.body().string();
        //Response message you get in which include app url that need to be passed in the capability matrix in the test
        System.out.println("response last:"+responsebody4);



        }


















    }

