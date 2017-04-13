<div class="topmargin15" id="termsModule" style=" padding-left: 42px;">
				<div id="action">	<p class="bold">Action:  
					<select id="termAction" name="termAction" class="invite_vote">	
						<option value="0">Select action</option>
						<!--  <option value="1">Delete</option>-->
						<option value="2">Invite to vote</option>
					</select>
					 </div>
					<div class="topmargin10 langSlctDropdwn bottommargin10 termLangSlctDropdwn">
						<select size="5" name="example-basic" multiple="multiple" title=" select language" id="termlanguageSlct"></select>
						&nbsp<input type="text" id="newTermVoted" value="Enter term ..." size="30" style="height: 18px; font-size:11px; font-weight:bold; width:198px;">
						&nbsp<input type="button" value="Search" id="searchTermVoted" class="commonBtn"  >
						&nbsp<input type="button" value="Delete All" class="commonBtn" name="deleteAllTerms" id="deleteAllTerms"/>
					</div>
						<div  id="termCompanySlct" class="topmargin10 companyTermsSlctDropdwn  bottommargin10 nodisplay">
									<select size="5" name="example-basic" multiple="multiple" title=" select company" id="companyTermsSlct"></select>
								</div>
					
					<div  class="bottommargin10 termsBtn"  style="text-align: right;width:950px;">
							<span style="float: left; padding-left: 4px;padding-top: 5px;"><input id = "showSelectedTerms" style="padding-left: 10px" type="checkbox"/><span style="font-size: 13px; font-weight: bold; "> Show Selected Terms</span></span>
							<span class="bold">Pagination: </span>
						   <select class="Page" id="paginationId" name="pagination">
						   <option value="Select" selected="selected">Select</option>
		                   <option value="10" >10</option>
		                   <option value="25">25</option>
		                   <option value="50">50</option>
		                   <option value="100">100</option>
		                   </select>
						<input type="button" value="Delete Selected" class="commonBtn" name="deleteMultipleTerms" id="deleteMultipleTerms" style="padding:3px 10px;" />&nbsp;&nbsp;
						<input type="button" value="More Data" class="commonBtn" name="moreData" id="moreData" style="padding:3px 10px;">
						<ul class="fieldsConfig nodisplay">
							<li class="pollExp">
								<label><input type="checkbox" name="highlightField" value="19"> &nbsp; <span>Poll expiration</span></label>
							</li>
							<li>
								<label><input type="checkbox" name="highlightField" value="20"> &nbsp; <span>Part of speech</span></label>
							</li>
							<li>
								<label><input type="checkbox" name="highlightField" value="21"> &nbsp; <span>Category</span></label>
							</li>
							<li>
								<label><input type="checkbox" name="highlightField" value="22"> &nbsp; <span>Domain</span></label>
							</li>
							<li class="selectAllBtn">
								<input type="button" value="Select All" class="commonBtn selectAllBtn" name="selectAllBtn" id="selectAllBtn">&nbsp; &nbsp;<input type="button" value="None" class="commonBtn none" name="none" id="none">
							</li>
							<li class="applyBtn">
								<input type="button" value="Apply" class="commonBtn" name="apply" id="apply">
							</li>
						</ul> 
			         </div>
				
					</p>
					<div id="manageTermTbl" class='bottommargin10 topmargin15'>
						<div id='mngTrmDtlSectionHead'>
					        <div id='column0' class='width25 noWidth'><input type="checkbox" id="selectAll" title="Select all" style="margin-top:1px;margin-left:7px;"/></div>
							<div id='targetTerm' class='width110' sortOrder="ASC" style="white-space: nowrap;margin-left: 22px;">Terms being polled</div>
							 <div id='column2' class='width40' >&nbsp;</div>
							<div id='suggestedTerm' class='width90' sortOrder="ASC">Top suggestion</div>
							<div id='language' class='width90' sortOrder="ASC">Language</div>
							<div id='status' class='width40' style="text-align:center" sortOrder="ASC">Final</div>
							<div id='pollExpirationDate' class='width90' sortOrder="ASC">Poll expiration</div>
							<div id='POS' class='width90' sortOrder="ASC">Part of speech</div>
							<div id='category' class='width110' style="text-align:center" sortOrder="ASC">Category</div>
							<div id='domain' class='width90' sortOrder="ASC">Domain</div>
                            <div id='status' class='width80 noWidth' sortOrder="ASC">Status</div>
							<div id='termEdit' class='width40 aligncenter noWidth' sortOrder="ASC">Edit</div>
							<div id='termDelete' class='width30 noWidth' sortOrder="ASC">Delete</div>
						</div>
						<div id='termDtlRowsList' >
							
						</div>
						<div id="pagination" class="pageination">
							<div style="color: #999999; float:left">Viewing <span id="rangeOfTerms">0 </span> of <span id="totalPolledTerms">0</span></div>
							<div style="float: right;"><span class="rightmargin5 termprevious" >Previous</span> | <span class="leftmargin5 termNext">Next</span></div>
						</div>
					</div>
					
				</div>