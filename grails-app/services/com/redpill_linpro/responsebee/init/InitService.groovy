package com.redpill_linpro.responsebee.init

import grails.transaction.Transactional

@Transactional
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
