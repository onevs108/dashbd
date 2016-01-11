/**
 * Googlemap setting
 */
var GOOGLE_MAP = {
		API_KEY: 'AIzaSyDVeFXi2ufABZk2qH359_JnHJ-BlHrkrCo',
		MAP_CENTER: {
			lat: 37.5462,
			lng: 126.9879
		},
		ZOOM_LEVEL: 12,
		CLUSTERER_MAX_ZOOM_LEVEL: 15
	};

/**
 * Logger
 */
var LOGGER = {
		USE_LOG: true
	};

/**
 * Users
 */
var USER = {
		DEFAULT_PAGE_ROWS: 10
	};

// 추후 DB에서 조회하는 방식으로 변경 필요
var area_positions = [];
area_positions["02"] = {lat: 37.549761, lng: 126.991185};
area_positions["031"] = {lat: 37.420170, lng: 127.510520};
area_positions["032"] = {lat: 37.453885, lng: 126.707021};
area_positions["033"] = {lat: 37.826329, lng: 128.150706};
area_positions["041"] = {lat: 36.716505, lng: 126.795210};
area_positions["042"] = {lat: 36.335924, lng: 127.392935};
area_positions["043"] = {lat: 37.003972, lng: 127.699962};
area_positions["051"] = {lat: 35.164310, lng: 129.045659};
area_positions["052"] = {lat: 35.545247, lng: 129.254700};
area_positions["053"] = {lat: 35.829107, lng: 128.565520};
area_positions["054"] = {lat: 36.299079, lng: 128.882347};
area_positions["055"] = {lat: 35.462819, lng: 128.209498};
area_positions["061"] = {lat: 34.875420, lng: 126.987919};
area_positions["062"] = {lat: 35.153629, lng: 126.834405};
area_positions["063"] = {lat: 35.723109, lng: 127.154498};
area_positions["064"] = {lat: 33.421225, lng: 126.795606};
area_positions["서울"] = {lat: 37.549761, lng: 126.991185};
area_positions["경기"] = {lat: 37.420170, lng: 127.510520};
area_positions["인천"] = {lat: 37.453885, lng: 126.707021};
area_positions["강원"] = {lat: 37.826329, lng: 128.150706};
area_positions["충남"] = {lat: 36.716505, lng: 126.795210};
area_positions["대전"] = {lat: 36.335924, lng: 127.392935};
area_positions["충북"] = {lat: 37.003972, lng: 127.699962};
area_positions["부산"] = {lat: 35.164310, lng: 129.045659};
area_positions["울산"] = {lat: 35.545247, lng: 129.254700};
area_positions["대구"] = {lat: 35.829107, lng: 128.565520};
area_positions["경북"] = {lat: 36.299079, lng: 128.882347};
area_positions["경남"] = {lat: 35.462819, lng: 128.209498};
area_positions["전남"] = {lat: 34.875420, lng: 126.987919};
area_positions["광주"] = {lat: 35.153629, lng: 126.834405};
area_positions["전북"] = {lat: 35.723109, lng: 127.154498};
area_positions["제주"] = {lat: 33.421225, lng: 126.795606};
