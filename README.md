# URL Shortener with QR Code Generation (Production Grade)

Make a production grade url shortner, and Implement a Dto layer for extra safety and return BASEAPIRESPONSE in every api request which make the response consistent .
Every APi Response is a same time which make Fronentend to Integrate easily.

Now User Give request like this :-

{
  "url":"https://www.microsoft.com/en-us/edge/update/145?ep=1773&es=315&form=MT00ZZ&channel=stable&version=145.0.3800.58&sg=2&cs=350088316",<br>
  "check": 2
}

In Response 
{
  "sucess": true,<br>
  "message": "Short url saved sucessfully",<br>
  data:{<br>
        "shortUrl":"http://localhost:1050/ajsd09,<br>
        "imageUrl:"https:fileCompress/file-upload/dev/locals/images/imagename,<br>
        }
}


** Some Points to reads for better understanding :-
* Use RestTemplate and RestClient object for calling or Integrate the some extrenal api in our project same like microservice architeture, Think like this
* My file upload service is already created in my other project and i just Integrate that api in my project That api take particular type of Request from user.
  so i created that one class -> QRUploadRequest.
* Make check i my url already present in my Db than it return from there.
* Let suppose a condition my shorturl generate using random UUID.RandomUUId function, now many hits it genarate the same random code which already present in Db than its an Error
  so there is code which 1st check that random code in db then genarate the random shorturl.
* Qrcode is generate in InMemory and Directly upload that to uploadApi which make Upload process fast.



