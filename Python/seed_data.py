import os
import sys
import django
import struct
import zlib

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
os.chdir(BASE_DIR)
sys.path.insert(0, BASE_DIR)
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'project.settings')
django.setup()

from apps.models import User, Complaint, SafeZones, Message
from django.utils.timezone import now

print("=== Seeding demo data ===")

# ── Create placeholder image ──────────────────────────────
os.makedirs("apps/static/images", exist_ok=True)
placeholder = "apps/static/images/demo.png"
if not os.path.exists(placeholder):
    def make_png():
        sig = b'\x89PNG\r\n\x1a\n'
        ihdr_d = struct.pack('>IIBBBBB', 1, 1, 8, 2, 0, 0, 0)
        ihdr_c = zlib.crc32(b'IHDR' + ihdr_d) & 0xffffffff
        ihdr = struct.pack('>I', 13) + b'IHDR' + ihdr_d + struct.pack('>I', ihdr_c)
        raw = zlib.compress(b'\x00\xff\x00\x00')
        idat_c = zlib.crc32(b'IDAT' + raw) & 0xffffffff
        idat = struct.pack('>I', len(raw)) + b'IDAT' + raw + struct.pack('>I', idat_c)
        iend_c = zlib.crc32(b'IEND') & 0xffffffff
        iend = struct.pack('>I', 0) + b'IEND' + struct.pack('>I', iend_c)
        return sig + ihdr + idat + iend
    with open(placeholder, 'wb') as f:
        f.write(make_png())
IMG = "static/images/demo.png"

# ── Users ─────────────────────────────────────────────────
users_data = [
    dict(name="Admin",         mobile="9999999999", mail="admin@gmail.com",  password="admin@123",  user_type="admin",     location="17.385044,78.486671", city="Hyderabad"),
    dict(name="LifeHelp NGO",  mobile="9876543210", mail="ngo@gmail.com",    password="ngo@123",    user_type="NGO",       location="17.385044,78.486671", city="Hyderabad"),
    dict(name="SafeCity NGO",  mobile="9876543211", mail="ngo2@gmail.com",   password="ngo@123",    user_type="NGO",       location="17.385044,78.486671", city="Hyderabad"),
    dict(name="Ravi Kumar",    mobile="9874563210", mail="vol@gmail.com",    password="vol@123",    user_type="Volunteer", location="17.385044,78.486671", city="Hyderabad"),
    dict(name="Priya Sharma",  mobile="9874563211", mail="vol2@gmail.com",   password="vol@123",    user_type="Volunteer", location="17.385044,78.486671", city="Hyderabad"),
    dict(name="Vasavi",        mobile="9123456789", mail="user@gmail.com",   password="user@123",   user_type="user",      location="17.385044,78.486671", city="Hyderabad"),
    dict(name="Ananya Reddy",  mobile="9123456780", mail="user2@gmail.com",  password="user@123",   user_type="user",      location="17.385044,78.486671", city="Hyderabad"),
]

for u in users_data:
    if not User.objects.filter(mail=u["mail"]).exists():
        User.objects.create(**u)
        print(f"  + User: {u['name']} ({u['user_type']})")
    else:
        print(f"  = Exists: {u['name']}")

ngo1 = User.objects.filter(mail="ngo@gmail.com").first()
vol1 = User.objects.filter(mail="vol@gmail.com").first()
user1 = User.objects.filter(mail="user@gmail.com").first()
user2 = User.objects.filter(mail="user2@gmail.com").first()

# ── Complaints ────────────────────────────────────────────
if Complaint.objects.count() == 0:
    complaints_data = [
        dict(title="Flood Alert",        description="Heavy flooding near MG Road, residents need evacuation.",    location_address="MG Road, Hyderabad",         lat_lon="17.4160,78.4762", ngoid=str(ngo1.id), volunteer_id=str(vol1.id), user_id=str(user1.id), status="Completed",  imagePath=IMG),
        dict(title="Fire Emergency",     description="Building fire reported near Jubilee Hills.",                  location_address="Jubilee Hills, Hyderabad",   lat_lon="17.4300,78.4000", ngoid=str(ngo1.id), volunteer_id=str(vol1.id), user_id=str(user2.id), status="Accepted",   imagePath=IMG),
        dict(title="Road Accident",      description="Major accident on NH44 near Shamshabad.",                    location_address="NH44, Shamshabad",           lat_lon="17.2350,78.4290", ngoid=str(ngo1.id), volunteer_id="",           user_id=str(user1.id), status="Pending",    imagePath=IMG),
        dict(title="Medical Emergency",  description="Multiple injured after building collapse in Old City.",       location_address="Old City, Hyderabad",        lat_lon="17.3600,78.4700", ngoid="",            volunteer_id="",           user_id=str(user2.id), status="Pending",    imagePath=IMG),
        dict(title="Earthquake Tremors", description="Mild tremors felt across Secunderabad. People need shelter.", location_address="Secunderabad, Hyderabad",   lat_lon="17.4400,78.4980", ngoid=str(ngo1.id), volunteer_id=str(vol1.id), user_id=str(user1.id), status="Accepted",   imagePath=IMG),
    ]
    for c in complaints_data:
        Complaint.objects.create(raised_on=now(), **c)
        print(f"  + Complaint: {c['title']} [{c['status']}]")
else:
    print(f"  = Complaints already exist ({Complaint.objects.count()} found)")

# ── Safe Zones ────────────────────────────────────────────
safe_zones_data = [
    dict(title="KIMS Hospital",        details="24/7 Emergency hospital with trauma center",       LatLon="17.4156,78.4500"),
    dict(title="Hyderabad Police HQ",  details="Main police HQ — call 100 for emergencies",        LatLon="17.3850,78.4867"),
    dict(title="Red Cross Center",     details="Emergency relief and medical assistance center",    LatLon="17.4000,78.4700"),
    dict(title="Gandhi Hospital",      details="Government hospital, free emergency treatment",     LatLon="17.3950,78.4750"),
]
for s in safe_zones_data:
    if not SafeZones.objects.filter(title=s["title"]).exists():
        SafeZones.objects.create(**s)
        print(f"  + SafeZone: {s['title']}")

# ── Emergency Message ─────────────────────────────────────
if Message.objects.count() == 0:
    Message.objects.create(title="Flood Warning", message="Stay indoors. Avoid low-lying areas. Emergency teams deployed.", city="Hyderabad", raiseOF="Admin")
    print("  + Emergency message created")

print("\n=== All done! ===")
print("\nLOGIN CREDENTIALS:")
print("  Admin    : admin@gmail.com  / admin@123")
print("  NGO      : ngo@gmail.com    / ngo@123")
print("  Volunteer: vol@gmail.com    / vol@123")
print("  User     : user@gmail.com   / user@123")
