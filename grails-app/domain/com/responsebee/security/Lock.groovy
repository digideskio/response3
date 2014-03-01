package com.responsebee.security

import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib

class Lock {
    
    User lockedBy
    Date dateCreated

    static constraints = {
        lockedBy(nullable:false)
    }
    
    static mapping = {
        table 'lockdata'
        version false
        id generator: 'sequence', params: [sequence: 'lockdata_seq']
        //Indexes
        id index:'lockdata_id_idx'
        lockedBy index:'lockdata_lockedBy_idx'
        cache usage:'read-write'
    }
    
    String toString() {
        def g = new ValidationTagLib()
        return g.message(
            code: "locked.by.info", args:[this.lockedBy, this.dateCreated])
    }
}
