@RestController
class InsideTweetRun {

	@RequestMapping("/")
	def home() {
		[app: 'spring-boot', desc: "Smaller than a tweet"]
	}

}
