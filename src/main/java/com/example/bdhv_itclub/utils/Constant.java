package com.example.bdhv_itclub.utils;

public class Constant {
    public static final String SUBJECT_REGISTER = "Please verify your registration to continue";
    public static final String SUBJECT_RESET = "Here's the link to reset your password";
    public static final String SUBJECT_ORDER = "Confirmation of your order ID #[[orderId]]";
    public static final String EMAIL_TEMPLATE_REGISTER ="<div style=\"font-size: 16px; letter-spacing: normal;\">Dear [[name]]," +
            "</div><div style=\"font-size: 16px; letter-spacing: normal;\"><i><br></i></div>" +
            "<div style=\"font-size: 16px; letter-spacing: normal;\">" +
            "<i>Click the link below to verify your registration:</i></div>" +
            "<div style=\"font-size: 16px; letter-spacing: normal;\"><i><br></i></div>" +
            "<a href=\"[[URL]]\" target=\"_self\" style=\"color: rgb(0, 123, 255); background-color: " +
            "transparent; font-size: 16px; letter-spacing: normal;\">VERIFY</a><div style=\"font-size: 16px;" +
            " letter-spacing: normal;\"><span style=\"font-size: 18px;\"><span style=\"font-size: 24px;\">" +
            "<span style=\"font-weight: bolder;\"><font color=\"#ff0000\"></font></span></span>" +
            "</span></div><div style=\"font-size: 16px; letter-spacing: normal;\"><br></div>" +
            "<div style=\"font-size: 16px; letter-spacing: normal;\">Thanks,</div>" +
            "<div style=\"font-size: 16px; letter-spacing: normal;\">The Tech Courses Team</div>";

    public static final String CONTENT_RESET = "<p>Hello [[name]],</p>" +
            "<p>You have requested to reset your password.</p>" +
            "<p>Click the link below to change your password:</p>" +
            "<p><a href=\"[[URL]]\">Change my password</a></p>" +
            "<br>" +
            "<p>Ignore this email if you do remember your password, " +
            "or you have not made the request.</p>";

    public static final String CONTENT_ORDER = """
            <div><div><div><b><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">Dear [[name]]</span></b><span style="font-size:
             12.0pt;font-family:&quot;Times New Roman&quot;,serif;mso-fareast-font-family:&quot;Times New Roman&quot;"><o:p></o:p></span></div>
            \s
             <div><b><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">This email is to confirm that you have
             successfully purchase. Please review the
             following order summary.</span></b><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><span style="font-size: 12pt;">&nbsp;</span></div>
            \s
             <div><!--[if !supportLists]--><span style="font-size: 10pt; font-family: Symbol;">·<span style="font-variant-numeric: normal; font-variant-east-asian: normal; font-variant-alternates: normal; font-kerning: auto; font-optical-sizing: auto; font-feature-settings: normal; font-variation-settings: normal; font-variant-position: normal; font-stretch: normal; font-size: 7pt; line-height: normal; font-family: &quot;Times New Roman&quot;;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             </span></span><!--[endif]--><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">&nbsp;Order
             ID: [[orderId]]</span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><!--[if !supportLists]--><span style="font-size: 10pt; font-family: Symbol;">·<span style="font-variant-numeric: normal; font-variant-east-asian: normal; font-variant-alternates: normal; font-kerning: auto; font-optical-sizing: auto; font-feature-settings: normal; font-variation-settings: normal; font-variant-position: normal; font-stretch: normal; font-size: 7pt; line-height: normal; font-family: &quot;Times New Roman&quot;;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             </span></span><!--[endif]--><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">&nbsp;Order
             time: [[orderTime]]</span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><!--[if !supportLists]--><span style="font-size: 10pt; font-family: Symbol;">·<span style="font-variant-numeric: normal; font-variant-east-asian: normal; font-variant-alternates: normal; font-kerning: auto; font-optical-sizing: auto; font-feature-settings: normal; font-variation-settings: normal; font-variant-position: normal; font-stretch: normal; font-size: 7pt; line-height: normal; font-family: &quot;Times New Roman&quot;;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             </span></span><!--[endif]--><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">&nbsp;Course
             name: [[courseName]]</span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><!--[if !supportLists]--><span style="font-size: 10pt; font-family: Symbol;">·<span style="font-variant-numeric: normal; font-variant-east-asian: normal; font-variant-alternates: normal; font-kerning: auto; font-optical-sizing: auto; font-feature-settings: normal; font-variation-settings: normal; font-variant-position: normal; font-stretch: normal; font-size: 7pt; line-height: normal; font-family: &quot;Times New Roman&quot;;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             </span></span><!--[endif]--><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">&nbsp;Total:
             [[total]] VND</span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;"><br>
             <!--[if !supportLineBreakNewLine]--><br>
             <!--[endif]--></span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">You can now start studying this course. When you complete you will receive a certificate for this course.</span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;"><br>
             <!--[if !supportLineBreakNewLine]--><br>
             <!--[endif]--></span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">Thanks for purchasing products at Tech Courses.</span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><span style="font-size: 12pt; font-family: &quot;Courier New&quot;; color: black; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">The Tech Course Team.</span><span style="font-size: 12pt;"><o:p></o:p></span></div>
            \s
             <div><span style="font-size:15.0pt;
             font-family:&quot;Times New Roman&quot;,serif">&nbsp;</span></div></div></div>""";

}
