package com.redpill_linpro.response3.init

class InitService {
    
    def contentService
    def usersService
    def responseClientService
    
    def init() {
        responseClientService.init()
        usersService.init()
        contentService.init()
    }
}
