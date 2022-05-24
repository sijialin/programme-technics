package be.kuleuven.mytomato;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.mytomato.database.ToDo;


public class User extends Application{
    private String userName;
    private int userID;
    private String password;
    private int gender;
    private String registerDate;
    private String whats_up;
    private String  photo;

    public List<ToDo> ToDoList;
    public  List<Integer> idList;

    private RequestQueue requestQueue;
    private static final String UPDATE_USERNAME_URL="https://studev.groept.be/api/a21pt321/update_userName/";
    private static final String UPDATE_PASSWORD_URL="https://studev.groept.be/api/a21pt321/update_password/";
    private static final String UPDATE_PHOTO_URL="https://studev.groept.be/api/a21pt321/update_photo/";
    private static final String UPDATE_WHATSUP_URL="https://studev.groept.be/api/a21pt321/update_whatsUp/";
    private static final String UPDATE_GENDER_URL="https://studev.groept.be/api/a21pt321/update_gender/";
    private static final String SEARCH_TODO_BY_USERID_URL="https://studev.groept.be/api/a21pt321/search_todo_by_userId/";
    private static final String INSERT_HABIT_URL="https://studev.groept.be/api/a21pt321/insert_toDo/";
    private static final String DELETE_TODO_BY_ID="https://studev.groept.be/api/a21pt321/delete_todo_by_id/";
    private static final String UPDATE_TODO_BY_ID="https://studev.groept.be/api/a21pt321/update_isDone/";
    private static final String UPDATE_DONETIMES_BY_ID = "https://studev.groept.be/api/a21pt321/update_donetimes_by_id/";
    private static final String EDIT_TODO_BY_ID="https://studev.groept.be/api/a21pt321/edit_todo_by_id/";

    public User(){
        ToDoList= new ArrayList<>();
        idList = new ArrayList<>();
    }

    public void initializing(int ID,String userName, String password,
                                 String registerDate,String photo, int gender, String whats_up){
        this.userName=userName;
        userID=ID;
        this.photo=photo;
        this.password=password;
        this.gender=gender;
        this.whats_up=whats_up;
        this.registerDate=registerDate;
    }


    public int deleteToDo(int index,DisplayActivity displayActivity){
        requestQueue= Volley.newRequestQueue(this);
        int id = idList.get(index);
        String deleteToDoURL=DELETE_TODO_BY_ID+id;
        StringRequest deleteToDoRequest=new StringRequest(Request.Method.GET,deleteToDoURL, response -> {
        }, error -> {
        });
        requestQueue.add(deleteToDoRequest);
        ToDoList.remove(index);
        idList.remove(index);
        displayActivity.onReady(getToDoList());
        return id;
    }

    //upload the new user photo to database
    public void setImage(Bitmap bitmap){
        requestQueue= Volley.newRequestQueue(this);
        //convert image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        String  updateUrl=UPDATE_PHOTO_URL+imageString+"/"+userID;
        //Execute the Volley call. Note that we are not appending the image string to the URL, that happens further below
        StringRequest  submitRequest = new StringRequest (Request.Method.POST, updateUrl,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                photo=imageString;
            }
        }, error -> error.printStackTrace()) { //NOTE THIS PART: here we are passing the parameter to the webservice, NOT in the URL!
                @Override
                //update photo according to id
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("photo", imageString);
                    params.put("id",Integer.toString(userID));
                    return params;
                }
            };
            requestQueue.add(submitRequest);
        }
        public void setUserName(String newUserName,UserActivity userActivity){
           requestQueue= Volley.newRequestQueue(this);
           String setUserNameURL=UPDATE_USERNAME_URL+newUserName+"/"+userID;
           StringRequest setUserNameRequest=new StringRequest(Request.Method.GET,setUserNameURL, response -> {
               userName=newUserName;
           }, error -> {
               });
           requestQueue.add(setUserNameRequest);
           userActivity.onReadyName(newUserName);
        }
         public boolean checkPassword(String InputPassword){
             return InputPassword.equals(password);
        }
        public void setPassword(String newPassword,UserActivity userActivity){
            requestQueue= Volley.newRequestQueue(this);
            String setURL=UPDATE_PASSWORD_URL+newPassword+"/"+userID;
            StringRequest setRequest=new StringRequest(Request.Method.GET,setURL, response -> {
                password=newPassword;
                userActivity.onReady();
            }, error -> {
            });
            requestQueue.add(setRequest);

        }
       public void setGender(int newGender,UserActivity userActivity){
           requestQueue= Volley.newRequestQueue(this);
           String setURL=UPDATE_GENDER_URL+newGender+"/"+userID;
           StringRequest setRequest=new StringRequest(Request.Method.GET,setURL, response -> {
               gender=newGender;
           }, error -> {
           });
           requestQueue.add(setRequest);
           userActivity.onReady();
        }

       public void setWhats_up(String newWhats_up,UserActivity userActivity){
           requestQueue= Volley.newRequestQueue(this);
           String setURL=UPDATE_WHATSUP_URL+newWhats_up+"/"+userID;
           StringRequest setRequest=new StringRequest(Request.Method.GET,setURL, response -> {
               whats_up=newWhats_up;
           }, error -> {
           });
           requestQueue.add(setRequest);
           userActivity.onReady();
        }

        public String  getUserName(){return userName;}
        public int getGender(){return gender;}
        public String getWhats_up(){return whats_up;}
        public int getUserID(){return userID;}
        public String getPhoto(){return  photo;}
        public String getPassword(){return password;}
        public String getRegisterDate(){return registerDate;}

        public boolean intToBoolean(int toConvert){
            return toConvert > 0;
        }
        public void downloadToDoList(MainActivity mainActivity){
            ToDoList.clear();
            idList.clear();
            requestQueue= Volley.newRequestQueue(this);
            String searchHabitByUserIDURL=SEARCH_TODO_BY_USERID_URL+userID;
            JsonArrayRequest searchHabitByUserIDRequest=new JsonArrayRequest(Request.Method.GET,
                    searchHabitByUserIDURL, null, response -> {
             for(int i=0;i<response.length();i++){
                 JSONObject o;
                 try{
                     o=response.getJSONObject(i);
                     int id = o.getInt("id");
                     ToDo todo=new ToDo(o.getInt("imageID"), (String) o.get("name"), (String) o.get("note"),
                             o.getInt("color"), (String) o.get("time"), (String) o.get("ddlDate"),
                             intToBoolean(o.getInt("label1")), intToBoolean(o.getInt("label2")),
                             intToBoolean( o.getInt("label3")), intToBoolean(o.getInt("label4")),
                             o.getInt("year"),o.getInt("month"), o.getInt("day"),
                             o.getInt("hour"), o.getInt("minute"),
                             o.getInt("doneTimes"),o.getInt("timesToDo"),o.getInt("isDone"));
                     ToDoList.add(todo);
                     idList.add(id);
                 }
                 catch (JSONException e){
                     e.printStackTrace();
                 }
             }
                mainActivity.onListReady(getToDoList());
             }, Throwable::printStackTrace);
            requestQueue.add(searchHabitByUserIDRequest);

        }

        private void updateIDlist(){
            idList.clear();
            requestQueue= Volley.newRequestQueue(this);
            String searchHabitByUserIDURL=SEARCH_TODO_BY_USERID_URL+userID;
            JsonArrayRequest searchHabitByUserIDRequest=new JsonArrayRequest(Request.Method.GET,
                    searchHabitByUserIDURL, null, response -> {
                for(int i=0;i<response.length();i++){
                    JSONObject o;
                    try{
                        o=response.getJSONObject(i);
                        int id = o.getInt("id");
                        idList.add(id);
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }, Throwable::printStackTrace);
            requestQueue.add(searchHabitByUserIDRequest);
        }

    public int booleanToInt(boolean toConvert){
           if(toConvert)
               return 1;
           else
               return 0;
        }
        //If the user already has this event and it hasn't been done, return false

        public void addToDo(ToDo newToDo,AddActivity addActivity){
           boolean duplicity=false;
           for(int i=0;i<ToDoList.size();i++){
               if(newToDo.getName().equals(ToDoList.get(i).getName())){
                    duplicity=true;
               }
           }
           if(!duplicity){
               requestQueue= Volley.newRequestQueue(this);
               String insertURL=INSERT_HABIT_URL+userID;
               insertURL+="/"+newToDo.getName();
               insertURL+="/"+newToDo.getNote();
               insertURL+="/"+newToDo.getColor();
               insertURL+="/"+newToDo.getImageID();
               insertURL+="/"+newToDo.getTime();
               insertURL+="/"+newToDo.getDate();
               insertURL+="/"+booleanToInt(newToDo.isLabel1());
               insertURL+="/"+booleanToInt(newToDo.isLabel2());
               insertURL+="/"+booleanToInt(newToDo.isLabel3());
               insertURL+="/"+booleanToInt(newToDo.isLabel4());
               insertURL+="/"+"0";
               //insertURL+="/"+"0";
               insertURL+="/"+newToDo.getTimesToDo();
               insertURL+="/"+"0";
               insertURL+="/"+newToDo.getYear();
               insertURL+="/"+newToDo.getMonth();
               insertURL+="/"+newToDo.getDay();
               insertURL+="/"+newToDo.getHour();
               insertURL+="/"+newToDo.getMinute();

               ToDoList.add(newToDo);
               StringRequest insertRequest=new StringRequest(Request.Method.GET,insertURL, response -> {
                   //ToDoList.add(newToDo);
               }, error -> {
               });
               requestQueue.add(insertRequest);
               updateIDlist();
           }

           addActivity.onReady();
        }

        public List<ToDo> getToDoList(){
            return ToDoList;
        }

    public void setToDoDone(int index,DisplayActivity displayActivity) {
        requestQueue= Volley.newRequestQueue(this);
        int id = idList.get(index);
        String updateIsDoneURL=UPDATE_TODO_BY_ID+id;
        StringRequest updateIsDoneRequest=new StringRequest(Request.Method.GET,updateIsDoneURL, response -> {
        }, error -> {
        });
        requestQueue.add(updateIsDoneRequest);
        ToDoList.remove(index);
        idList.remove(index);
        displayActivity.onReady(getToDoList());
    }

    public void setDoneOne(int index,DisplayActivity displayActivity) {
        requestQueue= Volley.newRequestQueue(this);
        int id = idList.get(index);
        int times = ToDoList.get(index).getDoneTimes()+1;
        ToDoList.get(index).setDoneTimes(times);
        String updateDoneTimesURL=UPDATE_DONETIMES_BY_ID+times+"/"+id;
        StringRequest updateDoneTimesRequest=new StringRequest(Request.Method.GET,updateDoneTimesURL, response -> {
        }, error -> {
        });
        requestQueue.add(updateDoneTimesRequest);
        displayActivity.onReady(getToDoList());
    }

    public List<ToDo> getRelevantToDos(String msg) {
            int i=0;
            List<ToDo> results = new ArrayList<>();
        switch (msg) {
            case "urgent":
                i = -2;
                break;
            case "important":
                i = -1;
                break;
            case "appointment":
                i = -3;
                break;
            case "email":
                i = -4;
                break;
            default:
                for (ToDo t : ToDoList) {
                    if (t.getName().equals(msg) || t.getNote().equals(msg)) {
                        results.add(t);
                    }
                }
                break;
        }
            if(i!=0 ) results = searchByLabels(i);
            return results;
    }

    private List<ToDo> searchByLabels(int i) {
        List<ToDo> results = new ArrayList<>();
        switch (i) {
            case -1:
                for(ToDo t:ToDoList){
                    if(t.isLabel1()) results.add(t);
                }
                break;
            case -2:
                for(ToDo t:ToDoList){
                    if(t.isLabel2()) results.add(t);
                }
                break;
            case -3:
                for(ToDo t:ToDoList){
                    if(t.isLabel3()) results.add(t);
                }
                break;
            case -4:
                for(ToDo t:ToDoList){
                    if(t.isLabel4()) results.add(t);
                }
                break;
            default:
                break;
        }
        return results;
    }

    public void editTime(int index,String time,String ddl,int year,int month,int day,
                         int hour,int minute,DisplayActivity displayActivity)
    {
        requestQueue= Volley.newRequestQueue(this);
        int id = idList.get(index);
        String editToDoURL=EDIT_TODO_BY_ID+
                time+"/"+ddl+"/"+year+"/"+month+"/"+day+"/"+hour+"/"+minute+"/"+id;
        StringRequest editToDoRequest=new StringRequest(Request.Method.GET,editToDoURL, response -> {
        }, error -> {
        });
        requestQueue.add(editToDoRequest);
        displayActivity.onReady(getToDoList());
    }
}


