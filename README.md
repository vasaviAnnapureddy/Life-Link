# LifeLink — AI-Powered Crisis Management System

A full-stack disaster response platform built with **Android (Kotlin)** + **Django (Python)** featuring an autonomous **Gemini AI Crisis Triage Agent**.

---

## Features

| Role | Capabilities |
|------|-------------|
| **Admin** | Manage NGOs, send emergency alerts, view complaints map, AI crisis report, compensation requests, volunteer/NGO lists, safe hubs |
| **NGO** | Accept/manage complaints, add volunteers, view resources & safe zones, view volunteer list |
| **Volunteer** | View & accept complaints, log needs, view safe zones |
| **User** | Submit text & video complaints, SOS alerts, view safe zones |

### AI Features
- **Autonomous Crisis Triage Agent** — Gemini 2.5 Flash analyses all active incidents and generates a prioritised response plan
- **Real Hyderabad Data** — 15 real disaster incident types (Musi River flood, Charminar area fires, Begum Bazaar collapse, etc.)

---

## Stack

- **Android**: Kotlin, Navigation Components, ExoPlayer, Retrofit2, ViewBinding, RecyclerView
- **Backend**: Django 5.1, SQLite, WhiteNoise, Gunicorn
- **AI**: Google Gemini 2.5 Flash (free tier via REST API)
- **Maps**: OpenStreetMap via WebView

---

## Setup

### Backend (Django)

```bash
cd Python
pip install -r requirements.txt

# Create .env file with your Gemini key:
echo "GEMINI_API_KEY=your_key_here" > .env

python manage.py migrate
python seed_data.py        # basic demo data
python seed_hyderabad.py   # 15 real Hyderabad incidents

python manage.py runserver 0.0.0.0:5000
```

### Android App

1. Open `Android/Crisis` in Android Studio
2. In `app/src/main/java/com/crisis/dataLayer/ApiCall.kt`, set `BASEURL` to your server IP:
   ```kotlin
   val BASEURL = "http://YOUR_PC_IP:5000/"
   ```
3. Build & run on device

---

## Demo Login Credentials

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@crisis.com | admin123 |
| NGO | ngo1@crisis.com | ngo123 |
| Volunteer | vol1@crisis.com | vol123 |
| User | user1@crisis.com | user123 |

---

## Deployment (Render.com)

1. Connect this GitHub repo to Render
2. Set **Root Directory** → `Python`
3. Add environment variable: `GEMINI_API_KEY=your_key`
4. Deploy — the app will be live at `https://your-app.onrender.com`
5. Update `BASEURL` in `ApiCall.kt` to the Render URL, rebuild APK

---

## Project Structure

```
LifeLink/
├── Android/Crisis/          # Kotlin Android app
│   └── app/src/main/java/com/crisis/
│       ├── ui/admin/        # Admin dashboard & functions
│       ├── ui/ngo/          # NGO dashboard
│       ├── ui/volunteer/    # Volunteer dashboard
│       ├── ui/user/         # User dashboard
│       └── dataLayer/       # Retrofit API layer
└── Python/                  # Django backend
    ├── apps/                # Models, views, URLs
    ├── project/             # Django settings
    ├── seed_data.py         # Demo data seeder
    └── seed_hyderabad.py    # Real Hyderabad disaster data
```

---

*Built for AI & Data Science portfolio — demonstrates end-to-end mobile + backend + LLM integration.*
