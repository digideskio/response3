package com.redpill_linpro.response3.security

class ResponseClientFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {
                if (request.serverName.contains(".")) {
                    String clientName = request.serverName.substring(
                            0, request.serverName.indexOf(".")
                    )
                    request.currentClient =
                        ResponseClient.findByName(clientName, [cache: true])
                }
            }
        }
    }
}
