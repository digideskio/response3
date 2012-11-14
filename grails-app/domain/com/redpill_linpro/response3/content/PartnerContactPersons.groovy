package com.redpill_linpro.response3.content

import java.io.Serializable;
import org.apache.commons.lang.builder.HashCodeBuilder
import com.redpill_linpro.response3.security.Role;
import com.redpill_linpro.response3.security.User;
import com.redpill_linpro.response3.security.UserRole;

class PartnerContactPersons implements Serializable {

    Partner partner
    User contactPerson

    static constraints = {
        partner (nullable: false)
        contactPerson (nullable: false)
    }
    
    static mapping = {
        table 'partner_contact_persons'
        version false
        id composite: ['partner', 'contactPerson']
        //Indexes
        partner index:'partner_contact_persons_partner_id_idx'
        contactPerson index:'partner_contact_persons_contact_person_id_idx'
        cache usage:'read-write'
    }
    
    boolean equals(other) {
        if (!(other instanceof PartnerContactPersons)) {
            return false
        }
        other.contactPerson?.id == contactPerson?.id &&
            other.partner?.id == partner?.id
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (contactPerson) builder.append(contactPerson.id)
        if (partner) builder.append(partner.id)
        builder.toHashCode()
    }

    static PartnerContactPersons get(long userId, long partnerId) {
        find 'from PartnerContactPersons where '+ 
        'contactPerson.id=:cid and partner.id=:pid',
            [cid: userId, pid: partnerId]
    }

    static PartnerContactPersons create(
        User user, Partner partner, boolean flush = false) {
        new PartnerContactPersons(
            contactPerson: user, partner: partner
        ).save(flush: flush, insert: true)
    }

    static boolean remove(User user, Partner partner, boolean flush = false) {
        PartnerContactPersons instance = 
            PartnerContactPersons.findByContactPersonAndPartner(user, partner)
        if (!instance) {
            return false
        }
        instance.delete(flush: flush)
        true
    }

    static void removeAll(User user) {
        executeUpdate( 
            'DELETE FROM PartnerContactPersons WHERE contactPerson=:c', 
            [c: user]
        )
    }

    static void removeAll(Partner partner) {
        executeUpdate(
            'DELETE FROM PartnerContactPersons WHERE partner=:p', 
            [p: partner]
        )
    }
}
