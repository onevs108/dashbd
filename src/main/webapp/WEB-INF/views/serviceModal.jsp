<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- s : POPUP - Add Operator -->
<div class="modal inmodal" id="serviceModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" onclick="closeModal()">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title">Service Info</h4>
			</div>
			<form role="form" id="form" class="form-horizontal">
				<div class="modal-body">
					<fieldset>
						<div id="streamingArea" class="row">
							<video id="Video1" data-dashjs-player controls></video>
						</div>
						<div id="infoArea" class="row" style="display:none;">
							<div align="center">BroadCast Info Page</div> 
						</div>
					</fieldset>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-white" onclick="closeModal()">Close</button>
				</div>
			</form>
		</div>
	</div>
</div>
<!-- e : POPUP - Add Operator -->

<script type="text/javascript">
	function closeModal() {
		$("#serviceModal").modal('hide');
		$("video")[0].pause();
	}
</script>