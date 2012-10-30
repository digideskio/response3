package com.redpill_linpro.response3.init

class InitService {
    
    def contentService
    def usersService
    
    def init() {
        usersService.init()
        contentService.init()
    }
}
