<div id='footer'>
    <% if (!isHpCompany) { %>
	    <a href='#'>Info</a> | <a href='#'>Legal</a> | <span class='leftmargin10'>&copy; Welocalize 2012</span>
    <% } else { %>
		<div class='footerLogo floatleft'>
			<img alt="Logo" src="<%=application.getContextPath()%>/images/welocalize_logo_hp.png" border="0"/>
		</div>
		<div class="floatright">
			<div class="footerLinks"><a href='mailto: terminology@hp.com'>Contact us</a> | <a
					href='javascript:_HPEPOpenWindow("https://www.hp.com/go/privacy", "Privacy Statement");'>Privacy
				statement</a> | <a target="new window" href='http://www.hp.com'>hp.com</a></div>
			<div class="copyright">&copy; 2012 Hewlett Packard Development Company, LP.</div>
		</div>
    <% } %>
</div>