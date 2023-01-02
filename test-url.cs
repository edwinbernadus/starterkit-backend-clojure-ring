

POST http://localhost:3000/test-post
content-type: application/json

{
    "name": "sample",
    "time": "Wed, 21 Oct 2015 18:27:50 GMT"
}


POST http://localhost:3000/test-header
content-type: application/json

{
    "name": "sample",
    "time": "Wed, 21 Oct 2015 18:27:50 GMT"
}


// send get only with header
GET http://localhost:3000/test-header


// send get only
GET http://localhost:3000/request