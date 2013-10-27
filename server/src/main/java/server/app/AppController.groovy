package server.app

import javax.servlet.http.HttpServletRequest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import server.StorageService

@Controller
class AppController {

	@Autowired
	StorageService storage

	@RequestMapping(value="/upload", consumes="application/json", method=RequestMethod.POST)
	@ResponseBody
	void upload(@RequestBody GraffitiRequest data) {
		storage.save(data)
	}

	@RequestMapping(value="/uploadImage", method=RequestMethod.POST)
	@ResponseBody
	void uploadImage(HttpServletRequest request) {
		def bytes = request.inputStream.bytes
		storage.saveImage(bytes)
	}
}
