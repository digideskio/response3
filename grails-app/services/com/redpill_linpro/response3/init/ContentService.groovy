package com.redpill_linpro.response3.init

import com.redpill_linpro.response3.content.*
import com.redpill_linpro.response3.security.ResponseClient
import com.redpill_linpro.response3.security.User
import com.redpill_linpro.response3.security.Role

class ContentService {
    
    def userService

    def init() {
        createPartners()
        createCustomers()
        createProjects()
        createIssues()
    }
    
    def createPartners(){
        new Partner(
                responseClient: ResponseClient.get(3),
                name:'Redpill').save(flush:true)
        new Partner(
                responseClient: ResponseClient.get(1),
                name:'Varnish').save(flush:true)
        new Partner(
                responseClient: ResponseClient.get(1),
                name:'Mulesoft').save(flush:true)
        new Partner(
                responseClient: ResponseClient.get(1),
                name:'Springsource').save(flush:true)
        new Partner(
                responseClient: ResponseClient.get(2),
                name:'Mpx').save(flush:true)
        assert Partner.count() == 5
        createPartnerClients()
        createPartnerContacts()
    }
    
    def createPartnerClients(){
        def user = new User(
                responseClient: ResponseClient.get(1),
                username: 'redp', enabled: true, password: '1234567890',
                firstname: 'red', lastname: 'pill',
                email:'red@response3.org'
        )
        try{
            userService.create(user, Role.findByAuthority("ROLE_PARTNER"))
        } catch (RuntimeException e){
            throw new RuntimeException(e.message)
        }
        def partner = Partner.findByName('Redpill')
        partner.addToClients(user)
        partner.addToClients(User.findByUsername('mrpartner'))
        partner.save(flush:true)
    }
    def createPartnerContacts(){
        def partner = Partner.findByName('Redpill')
        def user = User.findByUsername('mrpartner')
        partner.addToContactPersons(user)
        partner.save(flush:true)
    }
    
    def createCustomers(){
        def customers = [
            'ABG Sundal Collier Holding',
            'AF Gruppen',
            'AGR Group',
            'AKVA Group',
            'Agasti Holding',
            'Aker',
            'Aker BioMarine',
            'Algeta',
            'American Shipping Company',
            'Apptix',
            'Archer ',
            'Arendals Fossekompani',
            'Atea',
            'Aurskog Sparebank',
            'Austevoll Seafood',
            'Avocet Mining',
            'BW Offshore Limited',
            'BWG Homes',
            'Bakkafrost',
            'Belships',
            'Bergen Group',
            'Bionor Pharma ',
            'Biotec Pharmacon',
            'Birdstep Technology',
            'Blom',
            'Bonheur',
            'Borgestad',
            'Borregaard',
            'Bouvet',
            'Byggma',
            'Cermaq',
            'Clavis Pharma',
            'Codfarmers',
            'Comrod Communication',
            'ContextVision',
            'Copeinca',
            'DNB',
            'DNB OBX',
            'DNB OBX Derivat BULL',
            'DNB OBX Derivat Bear',
            'DNO International',
            'DOF',
            'Data Respons',
            'Deep Sea Supply',
            'Det norske oljeselskap',
            'DiaGenic',
            'Dockwise',
            'Dolphin Group ',
            'Domstein',
            'EOC',
            'EVRY',
            'Eidesvik Offshore',
            'Eitzen Chemical',
            'Eitzen Maritime Services',
            'Ekornes',
            'Electromagnetic Geoservices',
            'Eltek',
            'Fairstar Heavy Transport',
            'Fara',
            'Farstad Shipping',
            'Fred. Olsen Energy',
            'Fred. Olsen Production',
            'Frontline',
            'Funcom',
            'GC Rieber Shipping',
            'Ganger Rolf',
            'Gjensidige Forsikring ',
            'Golden Ocean Group',
            'Goodtech',
            'Grieg Seafood',
            'Gyldendal',
            'Hafslund ser. A',
            'Hafslund ser. B',
            'Havila Shipping',
            'Helgeland Sparebank',
            'Hexagon Composites',
            'Hol Sparebank',
            'Hurtigruten',
            'Höegh LNG Holdings',
            'Høland og Setskog Sparebank',
            'I.M. Skaugen',
            'IGE Resources ',
            'Imarex',
            'Indre Sogn Sparebank',
            'Infratek',
            'InterOil Exploration and Production',
            'Intex Resources',
            'Itera ',
            'Jason Shipping',
            'Jinhui Shipping and Transportation',
            'Kitron',
            'Klepp Sparebank',
            'Kongsberg Automotive Holding',
            'Kongsberg Gruppen',
            'Kværner',
            'Lerøy Seafood Group',
            'Marine Harvest',
            'Medistim',
            'Melhus Sparebank',
            'Morpol ',
            'Namsos Trafikkselskap',
            'Navamedic',
            'Nes Prestegjelds Sparebank',
            'Nio',
            'Norda',
            'Nordic Semiconductor',
            'Norse Energy Corp.',
            'Norsk Hydro',
            'Norske Skogindustrier',
            'Northern Logistic Property',
            'Northern Offshore',
            'Northland Resources',
            'Norway Pelagic',
            'Norway Royal Salmon ',
            'Norwegian Air Shuttle',
            'Norwegian Car Carriers',
            'Norwegian Energy Company',
            'Norwegian Property',
            'OB TEST INSTRUMENT 1',
            'OB TEST INSTRUMENT 10',
            'OB TEST INSTRUMENT 2',
            'OB TEST INSTRUMENT 3',
            'OB TEST INSTRUMENT 4',
            'OB TEST INSTRUMENT 5',
            'OB TEST INSTRUMENT 6',
            'OB TEST INSTRUMENT 7',
            'OB TEST INSTRUMENT 8',
            'OB TEST INSTRUMENT 9',
            'Oceanteam Shipping',
            'Odfjell ser. A',
            'Odfjell ser. B',
            'Olav Thon Eiendomsselskap',
            'Opera Software',
            'Orkla',
            'PSI Group',
            'Panoro Energy',
            'Petroleum Geo-Services',
            'Petrolia E&P Holding',
            'Photocure',
            'Polarcus',
            'Polaris Media',
            'Pronova BioPharma',
            'Prosafe',
            'Protector Forsikring',
            'Q-Free',
            'Questerre Energy Corporation',
            'Renewable Energy Corporation',
            'Repant',
            'Reservoir Exploration Technology',
            'Rieber & Søn',
            'Rocksource',
            'Royal Caribbean Cruises',
            'SAS AB',
            'SalMar',
            'Sandnes Sparebank',
            'Scana Industrier',
            'Schibsted',
            'SeaBird Exploration',
            'Seadrill',
            'Selvaag Bolig ',
            'Sevan Drilling',
            'Sevan Marine',
            'Siem Offshore',
            'Siem Shipping',
            'SinOceanic Shipping',
            'Skiens Aktiemølle',
            'Solstad Offshore',
            'Solvang',
            'Songa Offshore',
            'SpareBank 1 Buskerud-Vestfold',
            'SpareBank 1 Nord-Norge',
            'SpareBank 1 Nøtterøy - Tønsberg',
            'SpareBank 1 Ringerike Hadeland',
            'SpareBank 1 SMN',
            'SpareBank 1 SR-Bank',
            'SpareBank 1 Østfold Akershus',
            'Sparebanken Møre',
            'Sparebanken Pluss',
            'Sparebanken Vest',
            'Sparebanken Øst',
            'Spectrum',
            'Statoil',
            'Stolt-Nielsen',
            'Storebrand',
            'Storm Real Estate',
            'Subsea 7',
            'TGS-NOPEC Geophysical Company',
            'TTS Group',
            'Telenor',
            'Telio Holding',
            'The Scottish Salmon Company',
            'Tide',
            'Tomra Systems',
            'Totens Sparebank',
            'Transit Invest',
            'Veidekke',
            'Veripos',
            'Vizrt',
            'Voss Veksel- og Landmandsbank',
            'Wentworth Resources',
            'Wilh. Wilhelmsen ',
            'Wilh. Wilhelmsen Holding ser. A',
            'Wilh. Wilhelmsen Holding ser. B',
            'Wilson',
            'XACT Bygg og Eiendom',
            'XACT Derivat BEAR',
            'XACT Derivat BULL',
            'XACT Energi',
            'XACT Finans',
            'XACT Industri',
            'XACT Konsum',
            'XACT Legemiddel',
            'XACT Materialer',
            'XACT Norden 120',
            'XACT OBX',
            'XACT Oil Service',
            'Yara International'
        ]
        customers.each{
            new Customer(
                    responseClient: ResponseClient.get(1),
                    partner:Partner.findByName('Redpill'),
                    name:it).save(flush:true)
        }
        customers = ['Aker Seafoods','Aker Solutions',]
        customers.each{
            new Customer(
                    responseClient: ResponseClient.get(1),
                    partner:Partner.findByName('Varnish'),
                    name:it).save(flush:true)
        }

        customers = ['Exxon','Schlumberger','British Petroleum']
        customers.each{
            new Customer(
                    responseClient: ResponseClient.get(3),
                    partner:Partner.findByName('Redpill'),
                    name:it).save(flush:true)
        }
    }
    def createProjects(){
        
    }
    def createIssues(){
        
    }
}
