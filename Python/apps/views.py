from django.http import JsonResponse
from django.shortcuts import render
from django.utils.timezone import now
from .models import User, Complaint, Message, MessageTypes, SafeZones, VideoUser
import base64
import time
from django.core import serializers
from django.views.decorators.csrf import csrf_exempt
import json
from django.db.models import Q
import os

def deleteSafeZone(request):
    commonJson = {"error": True, "message": "Success",}
    id=request.GET.get('id',0)
    try:
        SafeZones.objects.filter(id=int(id)).delete()
    except Exception as e:
        commonJson["message"]=str(e)
    return JsonResponse(commonJson)


def viewBothVolunteer(request):
    commonJson = {"error": True, "message": "Success","data":[]}
    typeOf=request.GET.get('typeOF','')
    try:
        data=User.objects.filter()
        view=[]
        for d in data:
            if typeOf == d.user_type:
                view.append({
                'name':d.name,
                'mobile':d.mobile,
                'mail':d.mail,
                'city':d.city
            })
            commonJson['data']=view
    except Exception as e:
        commonJson["message"]=str(e)
    return JsonResponse(commonJson)



def register(request):
    if request.method == "GET":
        commonJson = {"error": True, "message": "Success"}
        mail = request.GET.get("mail", "").strip()
        password = request.GET.get("password", "").strip()
        name = request.GET.get("name", "").strip()
        mobile = request.GET.get("mobile", "").strip()
        user_type = request.GET.get("type", "").strip()
        location = request.GET.get("location", "").strip()
        city = request.GET.get("city", "").strip()

        if User.objects.filter(mail=mail).exists():
            commonJson["message"] = "Try with another Mail-Id number"
        else:
            # Create new user
            User.objects.create(
                name=name,
                mobile=mobile,
                mail=mail,
                password=password,
                user_type=user_type,
                location=location,
                city=city,
            )
            commonJson["error"] = False
            commonJson["message"] = "User registered successfully"

        return JsonResponse(commonJson)


def login(request):
    commonJson = {"error": True, "message": "Success"}
    mail = request.GET.get("mail", "").strip()
    password = request.GET.get("password", "").strip()

    user = User.objects.filter(mail=mail, password=password).first()

    if user:
        commonJson["error"] = False
        commonJson["message"] = "Login successful"
        commonJson["data"] = [
            {
                "id": user.id,
                "name": user.name,
                "mail": user.mail,
                "type": user.user_type,
                "city": user.city,
                "mobile": user.mobile,
            }
        ]
    else:
        commonJson["message"] = "Invalid credentials"
    return JsonResponse(commonJson)


@csrf_exempt
def add_complaint(request):
    if request.method == "POST":
        commonJson = {"error": True, "message": "Complaint successfully added"}

        title = request.POST.get("title", "")
        description = request.POST.get("decriptionPoint", "")
        location_address = request.POST.get("locationsAddress", "")
        lat_lon = request.POST.get("latLon", "")
        raised_on = now()  # Current time
        ngoid = request.POST.get("ngoid", "")
        volunteer_id = request.POST.get("volunteerId", "")
        user_id = request.POST.get("userid", "")
        status = request.POST.get("status", "")
        userCheck = request.POST.get("userCheck", "")
        image = request.POST.get("image", "None")

        timeview = round(time.time() * 1000)
        path = f"apps/static/images/{timeview}.png"
        imagePoint = base64.b64decode(image)

        # Save the complaint
        with open(path, "+wb") as f:
            f.write(imagePoint)
        path = path.replace("apps/", "")
        complaint = Complaint(
            title=title,
            description=description,
            location_address=location_address,
            lat_lon=lat_lon,
            raised_on=raised_on,
            ngoid=ngoid,
            volunteer_id=volunteer_id,
            user_id=user_id,
            userCheck=userCheck,
            status=status,
            imagePath=path,
        )

        if image:
            complaint.image = image

        complaint.save()

        commonJson["error"] = False
        return JsonResponse(commonJson)


@csrf_exempt
def add_complaint_with_View(request):
    if request.method == "POST":
        commonJson = {"error": True, "message": "Complaint Successfully added"}
        userid = request.POST.get("userid", "").strip()
        desc = request.POST.get("desc", "").strip()

        uploaded_file = request.FILES.get("file")

        file_path = "None"

        if uploaded_file:
            timestamp = int(time.time() * 1000)
            file_ext = os.path.splitext(uploaded_file.name)[1]
            filename = f"{timestamp}{file_ext}.mkv"

            save_path = os.path.join("apps/static/uploads/", filename)
            os.makedirs(os.path.dirname(save_path), exist_ok=True)

            with open(save_path, "wb+") as destination:
                for chunk in uploaded_file.chunks():
                    destination.write(chunk)

            file_path = save_path.replace("apps/", "")

        complaint = VideoUser(userid=userid, path=file_path, description=desc)
        complaint.save()

        commonJson["error"] = False
        return JsonResponse(commonJson)


def get_count_of(request):
    commonJson = {"error": True, "message": "Success"}
    complaints = Complaint.objects.values("status").annotate(count=models.Count("id"))

    commonJson["data"] = list(complaints)
    return JsonResponse(commonJson)


def get_cities(request):
    cities = User.objects.values("city").distinct()
    return JsonResponse({"cities": list(cities)})


def updateCityAndGetMessages(request):
    commonJson = {"error": True, "message": "Success", "data": []}
    try:
        cityPoint = request.GET.get("city", "").strip()
        userid = request.GET.get("userid", 0)
        User.objects.filter(id=int(userid)).update(city=cityPoint)
        message = Message.objects.filter(city__icontains=cityPoint).last()
        messages = serializers.serialize("json", [message])
        data = json.loads(messages)[0]["fields"]
        data["id"] = message.id
        commonJson["data"] = data
    except Exception as e:
        commonJson = {"error": True, "message": f"{e}"}
    return JsonResponse(commonJson)


def viewComplaintOfUsers(request):
    commonJson = {"error": True, "message": "An error occurred"}

    try:
        user_id_point = request.GET.get("user_id", "")

        if not user_id_point:
            commonJson["message"] = "user_id is required."
            return JsonResponse(commonJson)

        complaints = Complaint.objects.filter(
            Q(user_id=user_id_point)
            | Q(ngoid=user_id_point)
            | Q(volunteer_id=user_id_point)
        )

        if not complaints.exists():
            commonJson["message"] = "No complaints found."
            return JsonResponse(commonJson)

        real = serializers.serialize("json", complaints)

        dataView = json.loads(real)

        data = [{"id": item["pk"], **item["fields"]} for item in dataView]

        commonJson["error"] = False
        commonJson["message"] = "Success"
        commonJson["data"] = data

    except Exception as e:
        commonJson["message"] = f"Error: {str(e)}"

    return JsonResponse(commonJson)


def get_user_details(request):
    try:
        commonJson = {"error": True, "message": "Success"}
        id = request.GET.get("id", "").strip()
        user = User.objects.filter(id=int(id)).first()

        commonJson["data"] = [
            {
                "id": user.id,
                "name": user.name,
                "mail": user.mail,
                "type": user.user_type,
                "city": user.city,
                "mobile": user.mobile,
            }
        ]
    except Exception as e:
        commonJson["message"] = e
    return JsonResponse(commonJson)


def sendMessage(request):
    commonJson = {"error": True, "message": "Success"}
    try:
        title = request.GET.get("title", "")
        message = request.GET.get("message", "")
        city = request.GET.get("city", "")
        raiseReturn = request.GET.get("raise", "")
        Message.objects.create(
            title=title, message=message, city=city, raiseOF=raiseReturn
        ).save()

    except Exception as e:
        commonJson["message"] = str(e)

    return JsonResponse(commonJson)


def get_unassigned_complaints_by_city(request):
    try:
        city = request.GET.get("city", "").strip()
        typeOfView = request.GET.get("typeOfView", "")
        userId=request.GET.get('userId','')
        print(typeOfView, city)
        if not city:
            return JsonResponse( 
                {"error": True, "message": "City parameter is required"}, status=400
            )
        if typeOfView == "":
            complaints = Complaint.objects.filter(
                ngoid="",
                user_id__in=User.objects.filter(city__icontains=city).values_list(
                    "id", flat=True
                ),
            )
        elif typeOfView=="jfkljfdg":
            complaints = Complaint.objects.filter(
                user_id=userId,
                user_id__in=User.objects.filter(city__icontains=city).values_list(
                    "id", flat=True
                ),
            )
        else:
            complaints = Complaint.objects.filter(
                volunteer_id="",
                user_id__in=User.objects.filter(city__icontains=city).values_list(
                    "id", flat=True
                ),
            )

        complaint_list = [
            {
                "id": complaint.id,
                "title": complaint.title,
                "description": complaint.description,
                "location_address": complaint.location_address,
                "lat_lon": complaint.lat_lon,
                "raised_on": complaint.raised_on,
                "status": complaint.status,
                "user_id": complaint.user_id,
                "ngoid": complaint.ngoid,
                "volunteer_id": complaint.volunteer_id,
                "imagePath": complaint.imagePath,
            }
            for complaint in complaints
        ]

        return JsonResponse(
            {"error": False, "message": "Success", "data": complaint_list}
        )

    except Exception as e:
        return JsonResponse({"error": True, "message": str(e)}, status=500)


def updateComplaint(request):
    commonJson = {"error": True, "message": "Success"}
    try:
        userid = request.GET.get("userid", 0)
        complaintID = request.GET.get("complaintID", 0)
        Complaint.objects.filter(id=int(complaintID)).update(ngoid=userid)

    except Exception as e:
        commonJson["message"] = e
    return JsonResponse(commonJson)


def updateComplaintVolunteer(request):
    commonJson = {"error": True, "message": "Success"}
    try:
        userid = request.GET.get("userid", 0)
        complaintID = request.GET.get("complaintID", 0)
        Complaint.objects.filter(id=int(complaintID)).update(volunteer_id=userid)

    except Exception as e:
        commonJson["message"] = e
    return JsonResponse(commonJson)


def completedComplaint(request):
    commonJson = {"error": True, "message": "Success"}
    try:
        idPoint = request.GET.get("complaintID", 0)

        Complaint.objects.filter(id=int(idPoint)).update(status="Completed")
    except Exception as e:
        commonJson["message"] = str(e)
    return JsonResponse(commonJson)


def complaintGraphDetails(request):
    commonJson = {"error": True, "message": "Success"}
    plaint = Complaint.objects.all()
    states = []

    for item in plaint:
        status_found = False
        for state in states:
            if state["status"] == item.status:
                state["count"] += 1
                status_found = True
                break
        if not status_found:
            states.append({"status": item.status, "count": 1})

    commonJson["data"] = states

    return JsonResponse(commonJson)


def viewComplaintOfLatLon(request):
    commonJson = {"error": True, "message": "An error occurred"}

    try:

        complaint = Complaint.objects.filter().all()

        real = serializers.serialize("json", complaint)
        dataView = json.loads(real)

        data = []
        for item in dataView:
            data.append({"id": item["pk"], **item["fields"]})

        commonJson["data"] = data
        commonJson["error"] = False
        commonJson["message"] = "Success"

    except Exception as e:
        commonJson["message"] = str(e)

    return JsonResponse(commonJson)


def sendSos(request):
    commonJson = {"error": True, "message": "Success"}
    try:
        title = request.GET.get("title", "").strip()
        message = request.GET.get("message", "").strip()
        userid = request.GET.get("userid", "").strip()
        locations = request.GET.get("locations", "").strip()
        typeOF = request.GET.get("typeOF", "").strip()

        MessageTypes.objects.create(
            title=title,
            message=message,
            userid=userid,
            locations=locations,
            typeOF=typeOF,
        )
    except Exception as e:
        commonJson["message"] = f"{e}"
    return JsonResponse(commonJson)


def getSosOFUserID(request):
    try:
        commonJson = {"error": True, "message": "Success", "data": []}
        user_id = request.GET.get("id", "").strip()

        if user_id:
            user_messages = MessageTypes.objects.filter(userid=user_id)
            if user_messages.exists():
                messages = []
                for message in user_messages:
                    message_data = {
                        "title": message.title,
                        "message": message.message,
                        "locations": message.locations,
                        "typeOF": message.typeOF,
                    }
                    messages.append(message_data)
                commonJson["data"] = messages
            else:
                commonJson["error"] = True
                commonJson["message"] = "No messages found for this user."
        else:
            commonJson["error"] = True
            commonJson["message"] = "Invalid user ID provided."

    except Exception as e:
        commonJson["error"] = True
        commonJson["message"] = str(e)

    return JsonResponse(commonJson)


def getWithUserDetails(request):
    try:
        commonJson = {"error": False, "message": "Success", "data": []}
    

        user = User.objects.filter()

        if user:
            user_messages = MessageTypes.objects.filter()

            if user_messages.exists():
                messages = []
                for message in user_messages:
                    view={}
                    for i in user:
                        if i.id==message.id:
                            view['id']=i.id
                            view['name']=i.name
                            view['mobile']=i.mobile
                            view['mail']=i.mail
                            view['user_type']=i.user_type
                            view['location']=i.location
                            view['city']=i.city
                    message_data = {
                        "title": message.title,
                        "message": message.message,
                        "locations": message.locations,
                        "typeOF": message.typeOF,
                        "user_details": view
                    }
                    messages.append(message_data)

                commonJson["data"] = messages
            else:
                commonJson["error"] = True
                commonJson["message"] = (
                    "No messages found for this user based on provided details."
                )
        else:
            commonJson["error"] = True
            commonJson["message"] = "User not found."

    except Exception as e:
        commonJson["error"] = True
        commonJson["message"] = f"An error occurred: {str(e)}"
    print(commonJson)
    return JsonResponse(commonJson)


def addSafeZone(request):
    commonJson = {"error": True, "message": "Success"}
    try:
        title = request.GET.get("title", "")
        details = request.GET.get("details", "")
        LatLon = request.GET.get("LatLon", "")
        SafeZones.objects.create(title=title, details=details, LatLon=LatLon)
    except Exception as e:
        commonJson["message"] = str(e)

    return JsonResponse(commonJson)


def getSafeZones(request):
    commonJson = {"error": True, "message": "Success", "data": []}
    try:
        safe_zones = SafeZones.objects.all()

        safe_zone_data_list = []
        for safe_zone in safe_zones:
            safe_zone_data = {
                "id": safe_zone.id,
                "title": safe_zone.title,
                "description": safe_zone.details,
                "location": safe_zone.LatLon,
            }
            safe_zone_data_list.append(safe_zone_data)
        commonJson["data"] = safe_zone_data_list

    except Exception as e:
        commonJson["message"] = str(e)
    return JsonResponse(commonJson)


@csrf_exempt
def getUploadedVideo(request):
    commonJson = {"error": True, "message": "Success", "data": []}
    try:
        userId = request.GET.get("userId", "")
        videoUser = VideoUser.objects.all()

        data = []
        for item in videoUser:
            if item.userid == userId:
                dataView = {
                    "id": item.id,
                    "desc":item.description,
                    "path":item.path
                }
                data.append(dataView)
        commonJson["data"] = data

    except Exception as e:
        commonJson["message"] = str(e)
    return JsonResponse(commonJson)

def getUploadedWithUserDetails(request):
    commonJson = {"error": True, "message": "Success", "data": []}
    try:
        videoUser = VideoUser.objects.all()

        data = []
        for item in videoUser:
            for details in User.objects.all():
                if str(details.id )== item.userid:
                    dataView = {
                    "id": item.id,
                    "desc":item.description,
                    "path":item.path,
                    "user":{
                        'name':details.name,
                        'mobile':details.mobile,
                        'mail':details.mail
                       }
                    }
                    data.append(dataView)
        commonJson["data"] = data

    except Exception as e:
        commonJson["message"] = str(e)
    return JsonResponse(commonJson)

def aiCrisisAgent(request):
    """
    Autonomous Crisis Response Agent
    Uses Gemini LLM to analyse all complaints and return:
    - Overall crisis level
    - Situation summary
    - Prioritised triage queue (severity 1-10)
    - Resource recommendations
    - 24-hour risk forecast

    RAG upgrade path: replace complaint_text below with
    vector-retrieved similar historical incidents for richer context.
    """
    import requests as req
    from django.conf import settings

    commonJson = {"error": True, "message": ""}
    try:
        api_key = getattr(settings, "GEMINI_API_KEY", "")
        if not api_key or api_key == "YOUR_GEMINI_API_KEY_HERE":
            return JsonResponse({"error": True,
                                 "message": "Gemini API key not set. Add GEMINI_API_KEY in settings.py"})

        all_c   = list(Complaint.objects.all())
        pending = [c for c in all_c if c.ngoid == ""]
        active  = [c for c in all_c if c.ngoid != "" and c.status != "Completed"]
        done    = [c for c in all_c if c.status == "Completed"]

        # Only send unassigned + brief summary of others to keep prompt short = faster
        complaint_text = "\n".join([
            f"[ID:{c.id}] {c.title} | {c.location_address} | {c.description[:80]}"
            for c in pending
        ])
        if not complaint_text:
            complaint_text = "No unassigned complaints right now."

        prompt = f"""You are an Autonomous Crisis Response AI Agent for LifeLink —
a real disaster management system deployed in Hyderabad, India.

LIVE SITUATION:
- Total incidents: {len(all_c)}
- UNASSIGNED (no NGO yet): {len(pending)}
- Active (being handled): {len(active)}
- Resolved: {len(done)}

ALL INCIDENT DATA:
{complaint_text}

Provide your full crisis assessment using EXACTLY this structure:

CRISIS LEVEL: [CRITICAL / HIGH / MEDIUM / LOW]

SITUATION SUMMARY:
[3 sentences — what is happening right now across Hyderabad]

TOP 3 IMMEDIATE ACTIONS:
1. [Most urgent unassigned incident — what to do NOW]
2. [Second most urgent]
3. [Third most urgent]

TRIAGE QUEUE (unassigned only, highest severity first):
ID | Incident | Severity /10 | Type | Recommended Action
[list each unassigned complaint]

RESOURCE RECOMMENDATION:
[NGOs needed, volunteers, equipment, medical units]

24-HOUR RISK FORECAST:
[Based on incident patterns, what should Hyderabad authorities prepare for]"""

        # Try models in order — falls back if one is overloaded
        models_to_try = ["gemini-2.5-flash", "gemini-2.0-flash", "gemini-2.0-flash-001"]
        resp = None
        for model in models_to_try:
            resp = req.post(
                f"https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent?key={api_key}",
                json={"contents": [{"parts": [{"text": prompt}]}]},
                timeout=30
            )
            if resp.status_code == 200:
                break
            if resp.status_code not in (503, 429):
                break  # non-retryable error

        if resp and resp.status_code == 200:
            report = resp.json()["candidates"][0]["content"]["parts"][0]["text"]
            return JsonResponse({
                "error": False,
                "report": report,
                "stats": {
                    "total": len(all_c),
                    "pending": len(pending),
                    "active": len(active),
                    "completed": len(done)
                }
            })
        else:
            code = resp.status_code if resp else 0
            msg  = resp.text[:200] if resp else "No response"
            return JsonResponse({"error": True,
                                 "message": f"Gemini error {code}: {msg}"})

    except Exception as e:
        commonJson["message"] = str(e)
        return JsonResponse(commonJson)


def updateAComplaintWithFeeBack(request):
        commonJson = {"error": True, "message": "Success"}
        try:
            id=request.GET.get('id',0)
            feedBack=request.GET.get('feedBack','')
            complaint=Complaint.objects.filter(id=int(id)).last()
            complaint.  volunterWrite=str(feedBack)
            complaint.save()
        except Exception as e:
            commonJson['message']=str(e)
        return JsonResponse(commonJson)


def submit_video_complaint(request):
    """Web form to submit video complaints - accessible via browser"""
    from django.http import HttpResponse
    html = """<!DOCTYPE html>
<html>
<head>
    <title>LifeLink - Report Video Complaint</title>
    <style>
        body { font-family: Arial; background: #f0f4ff; margin: 0; padding: 20px; }
        .container { max-width: 500px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        h1 { color: #1A237E; text-align: center; }
        label { display: block; margin: 15px 0 5px; font-weight: bold; color: #333; }
        input, textarea { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px; box-sizing: border-box; font-size: 14px; }
        textarea { resize: vertical; min-height: 80px; }
        button { width: 100%; padding: 12px; margin-top: 20px; background: #1A237E; color: white; border: none; border-radius: 5px; font-size: 16px; cursor: pointer; }
        button:hover { background: #0d1b5e; }
        .success { color: green; padding: 10px; background: #e8f5e9; border-radius: 5px; margin-bottom: 20px; display: none; }
        .error { color: red; padding: 10px; background: #ffebee; border-radius: 5px; margin-bottom: 20px; display: none; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🚨 Report Crisis Video</h1>
        <p style="text-align: center; color: #666;">Paste a video URL or describe an incident</p>
        <div class="success" id="success">✓ Video complaint submitted!</div>
        <div class="error" id="error"></div>
        <form id="videoForm">
            <label>Your User ID:</label>
            <input type="text" id="userid" placeholder="user1@crisis.com" required>
            <label>Video URL:</label>
            <input type="url" id="videourl" placeholder="https://example.com/video.mp4" required>
            <label>Description:</label>
            <textarea id="description" placeholder="What happened?" required></textarea>
            <button type="submit">Submit</button>
        </form>
    </div>
    <script>
        document.getElementById('videoForm').onsubmit = async (e) => {
            e.preventDefault();
            const data = {
                userid: document.getElementById('userid').value,
                videourl: document.getElementById('videourl').value,
                description: document.getElementById('description').value
            };
            try {
                const response = await fetch('/submit_video_complaint_api/', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(data)
                });
                const result = await response.json();
                if (result.error) {
                    document.getElementById('error').textContent = '✗ ' + result.message;
                    document.getElementById('error').style.display = 'block';
                } else {
                    document.getElementById('videoForm').reset();
                    document.getElementById('success').style.display = 'block';
                    setTimeout(() => { document.getElementById('success').style.display = 'none'; }, 3000);
                }
            } catch (err) {
                document.getElementById('error').textContent = '✗ Error: ' + err.message;
                document.getElementById('error').style.display = 'block';
            }
        };
    </script>
</body>
</html>"""
    return HttpResponse(html, content_type="text/html")


@csrf_exempt
def submit_video_complaint_api(request):
    """API endpoint to save video complaint from web form"""
    commonJson = {"error": True, "message": "Video saved successfully"}

    if request.method == "POST":
        try:
            data = json.loads(request.body)
            userid = data.get('userid', '').strip()
            videourl = data.get('videourl', '').strip()
            description = data.get('description', '').strip()

            if not userid or not videourl:
                commonJson["message"] = "User ID and video URL are required"
                return JsonResponse(commonJson)

            # Create VideoUser record
            video = VideoUser(userid=userid, path=videourl, description=description)
            video.save()

            commonJson["error"] = False
            commonJson["message"] = f"Video complaint submitted! ID: {video.id}"
            return JsonResponse(commonJson)

        except Exception as e:
            commonJson["message"] = str(e)
            return JsonResponse(commonJson)

    return JsonResponse({"error": True, "message": "GET /submit_video_complaint_web/ for form"})