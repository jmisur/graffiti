package server.web

import javax.servlet.http.HttpServletResponse

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

import server.StorageService
import server.repo.GraffitiData

@Controller
class FrontendController {

	@Autowired
	StorageService storage

	@RequestMapping(value = "/find", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	FindResponse find(@RequestParam("lat0") String lat0, @RequestParam("long0") String long0,
			@RequestParam("lat1") String lat1, @RequestParam("long1") String long1,HttpServletResponse response ){
		response.addHeader("Access-Control-Allow-Origin", "*");
		List<GraffitiData> data = storage.find(lat0, long0, lat1, long1)
		println data
		new FindResponse(data: data.collect {new GraffitiResponse(it)})
	}
}
