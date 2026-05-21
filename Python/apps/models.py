from django.db import models


class User(models.Model):
    id = models.IntegerField(auto_created=True, primary_key=True)
    name = models.CharField(max_length=255)
    mobile = models.CharField(max_length=15)
    mail = models.EmailField(unique=True)
    password = models.CharField(max_length=255)
    user_type = models.CharField(max_length=50)
    location = models.CharField(max_length=255)
    city = models.CharField(max_length=255)

    def __str__(self):
        return self.name


class Complaint(models.Model):
    id = models.IntegerField(auto_created=True, primary_key=True)
    title = models.CharField(max_length=255)
    description = models.TextField()
    location_address = models.CharField(max_length=255)
    lat_lon = models.CharField(max_length=255)
    raised_on = models.DateTimeField()
    ngoid = models.CharField(max_length=255)
    volunteer_id = models.CharField(max_length=255)
    user_id = models.CharField(max_length=255)
    status = models.CharField(max_length=50)
    volunterWrite = models.CharField(max_length=255, default="")
    volunteerCheck = models.CharField(max_length=255, default="")
    userCheck = models.CharField(max_length=255, default="")
    imagePath = models.CharField(max_length=3000)

    def __str__(self):
        return self.title


class Message(models.Model):
    id = models.IntegerField(auto_created=True, primary_key=True)
    title = models.CharField(max_length=255)
    message = models.CharField(max_length=255)
    city = models.CharField(max_length=255)
    raiseOF = models.CharField(max_length=255)

    def __str__(self):
        return "Messages"


class MessageTypes(models.Model):
    id = models.IntegerField(auto_created=True, primary_key=True)
    title = models.CharField(max_length=255)
    message = models.CharField(max_length=255)
    userid = models.CharField(max_length=255)
    locations = models.CharField(max_length=255)
    typeOF = models.CharField(max_length=255)

    def __str__(self):
        return "MessagesTypes"


class SafeZones(models.Model):
    id = models.IntegerField(auto_created=True, primary_key=True)
    title = models.CharField(max_length=255)
    details = models.CharField(max_length=255)
    LatLon = models.CharField(max_length=255)

    def __str__(self):
        return "SafeZones"


class VideoUser(models.Model):
    id=models.IntegerField(auto_created=True,primary_key=True,default=0)
    userid=models.CharField(max_length=500)
    path=models.CharField(max_length=500)
    description=models.CharField(max_length=500)
    
    def __str__(self):
        return "VideosOFUser"