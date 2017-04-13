<%@ page import="com.teaminology.hp.service.enums.TeaminologyProperty" %>
<div id='footer'>
    <% if (!isHpCompany) { %>
    <a href='#'>Info</a> | <a href='#'>Legal</a> | <span class='leftmargin10'>&copy; Welocalize 2012</span>   | <span><%=TeaminologyProperty.VERSION.getValue()%></span>
    <% } else { %>
    <div class='footerLogo floatleft'>
       <img alt="Logo" src="<%=application.getContextPath()%>/images/welocalize_logo_hp.png" border="0"/> 
       <%-- <span> <%=TeaminologyProperty.VERSION.getValue()%></span> --%>
    </div>
    <div style="padding-top: 8px; color: #5a5a5a; font-size: 16px;">Powered by Welocalize</div>
    <div class="floatrightForFooter">
        <div class="footerLinks"><a target="new window" href='https://www.welocalize.com' style="font-family: Hp Simplified; font-size: 16px; color: rgb(90, 90, 90);">Contact us</a> <!-- <!-- | <a
                href='javascript:_HPEPOpenWindow("https://www.hp.com/go/privacy", "Privacy Statement");' style="font-family: Hp Simplified; font-size: 16px; color: rgb(90, 90, 90);">Privacy
            statement</a> | <a target="new window" href='http://www.hp.com' style="font-family: Hp Simplified; font-size: 16px; color: rgb(90, 90, 90);">hp.com</a> --> </div>
        <!-- <div class="copyright">&copy; 2012 Hewlett Packard Development Company, LP.  </div> -->
    </div>
    <% } %>
