class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(controller:'default')
        "403"(view:'/error/forbidden')
        "500"(view:'/error')
        "404"(view:'/error/notfound')
	}
}
