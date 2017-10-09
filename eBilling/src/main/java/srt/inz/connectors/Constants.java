package srt.inz.connectors;


public interface Constants {

    //Progress Message
    String LOGIN_MESSAGE="Logging in...";
    String REGISTER_MESSAGE="Register in...";

    //Urls
    String BASE_URL="https://inzenjerebilling.000webhostapp.com/";
        
    String LOGIN_URL=BASE_URL+"conslogin.php?";   
    String BILLVIEW_URL=BASE_URL+"billvw.php?"; 
    String BILLGEN_URL=BASE_URL+"bilge.php?";
    String GETCUSTOMERLIST_URL=BASE_URL+"customlist.php?";
    String UPDATEREADING_URL=BASE_URL+"updatereading.php?"; 
    String REGISTER_URL=BASE_URL+"customreg.php?";    
        /*   String MONTHLYATTENDANCE_URL=BASE_URL+"stattendance_retnew.php?";   
    String ATTENDANCEUPDATE_URL=BASE_URL+"attendance_update.php?";
    String STUDENTDET_URL=BASE_URL+"student_det.php?";
    
   */
    
  //  example
    
    String EXAMPLE_URL=BASE_URL+"example.php?";
    

    //Details
    String SMSSERVICE="telSms";
    String ENUMBER="e_num";
    String ENUMBER1="e_num1";
    String ENUMBER2="e_num2";
    String LOGINSTATUS="LoginStatus";
    String MYLOCATON="MyLocation";
    String USERID="UserID";
    
    //SharedPreference
    String PREFERENCE_PARENT="Parent_Pref";

   
}
