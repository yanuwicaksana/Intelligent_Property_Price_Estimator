import json, requests


def FindNearbyInterest(documentNo):
    for i in range(documentNo):
        dirname = "../RetrievalResult/" + str(i+1)
        with open(dirname + "/result.json") as data_file:
            data = json.load(data_file)
        # print(data)
        Address = data.get('Address')
        Images = data.get('Images').split(",")


        url = 'https://maps.googleapis.com/maps/api/geocode/json'
        params = dict(address=Address, key='AIzaSyC9eOgeDEQCAy0T7ib7d1h27vtaUYqmick')

        resp = requests.get(url=url, params=params)
        data = json.loads(resp.content)
        result = data.get('results')
        geometry = result[0].get('geometry')
        location = geometry.get('location')
        lat = location.get('lat')
        long = location.get('lng')
        coordinates = str(lat) + "," + str(long)


        url2 = 'https://api.foursquare.com/v2/venues/explore'

        params2 = dict(
            client_id='K0P2VQLVVQW03RAYQPDKEHJC0TO1555UL2053TQGGWCVB0XK',
            client_secret='XGBRQYVAFY5I0ZKS33QFZP22ZRX011JOHPWIUPCIIX40J2ZQ',
            v='20170801',
            ll=coordinates
        )
        resp2 = requests.get(url=url2,params=params2)
        data2 = json.loads(resp2.content)
        print(data2)
        with open(dirname + '/NearbyPlace-' + str(i+1) +  '.json', 'w') as outfile:
            json.dump(data2, outfile)
FindNearbyInterest(10)