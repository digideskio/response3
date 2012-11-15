package com.redpill_linpro.response3.content

import java.io.Serializable;
import org.apache.commons.lang.builder.HashCodeBuilder
import com.redpill_linpro.response3.security.User

class PartnerClients implements Serializable{
    
    Partner partner
    User client

    static constraints = {
        partner(nullable: false)
        client(nullable: false, unique:true)
    }
    
    static mapping = {
        table 'partner_clients'
        version false
        id composite: ['partner', 'client']
        //Indexes
        partner index:'partner_clients_partner_id_idx'
        client index:'partner_clients_client_id_idx'
        cache usage:'read-write'
    }
    
    int hashCode() {
        def builder = new HashCodeBuilder()
        if (client) builder.append(client.id)
        if (partner) builder.append(partner.id)
        builder.toHashCode()
    }

    static PartnerClients get(long userId, long partnerId) {
        find 'from PartnerContactPersons where '+
        'client.id=:cid and partner.id=:pid',
            [cid: userId, pid: partnerId]
    }

    static PartnerClients create(
        User user, Partner partner, boolean flush = false) {
        new PartnerContactPersons(
            client: user, partner: partner
        ).save(flush: flush, insert: true)
    }

    static boolean remove(User user, Partner partner, boolean flush = false) {
        PartnerClients instance =
            PartnerClients.findByClientAndPartner(user, partner)
        if (!instance) {
            return false
        }
        instance.delete(flush: flush)
        true
    }

    static void removeAll(User user) {
        executeUpdate(
            'DELETE FROM PartnerClients WHERE client=:c',
            [c: user]
        )
    }

    static void removeAll(Partner partner) {
        executeUpdate(
            'DELETE FROM PartnerClients WHERE partner=:p',
            [p: partner]
        )
    }
}
