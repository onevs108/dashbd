<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="circle-map">
	<img src="/dashbd/resources/newPublish/img/subpage/img_circle_india_bk.png" style="width: 99%;" alt="" />
	<c:forEach var="circle" items="${circleList}" varStatus="status">
		<c:choose>
			<c:when test="${circle.sort != 99}">
				<a class="circle-item item${circle.sort}" data-init="${circle.circle_id}">
					<img src="/dashbd/resources/newPublish/img/subpage/img_india_item_${circle.circle_id}.png" alt="" />
					<span><small class="text-muted">${circle.circle_name} Telecom Circle</small></span>
				</a>
			</c:when>
			<c:when test="${circle.sort == 99}">
				<a class="circle-item item9" data-init="${circle.circle_id}" onclick="moveToCircle()">
					<img src="/dashbd/resources/newPublish/img/subpage/img_india_item_${circle.circle_id}.png" alt="" />
					<span><small class="text-muted">${circle.circle_name} Telecom Circle</small></span>
				</a>
			</c:when>
		</c:choose>
	</c:forEach>
</div>
