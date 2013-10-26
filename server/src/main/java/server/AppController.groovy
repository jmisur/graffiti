package server

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class AppController {

	@Autowired
	StorageService storage;

	@RequestMapping(value="/upload", consumes="application/json", method=RequestMethod.POST)
	@ResponseBody
	void upload(@RequestBody GraffitiRequest data) {
		storage.save(data)
	}
}
