package com.redpill_linpro.response3.init

class InitService {
    
    def contentService
    def usersService
    def responseClientService
    def roleService
    def adminUserService
    
    def init() {
        roleService.init()
        responseClientService.init()
        adminUserService.init()
        usersService.init()
        contentService.init()
    }
}
