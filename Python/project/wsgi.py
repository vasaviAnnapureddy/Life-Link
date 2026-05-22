"""
WSGI config for project project.

It exposes the WSGI callable as a module-level variable named ``application``.

For more information on this file, see
https://docs.djangoproject.com/en/5.1/howto/deployment/wsgi/
"""

import os
import django

from django.core.wsgi import get_wsgi_application

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'project.settings')

application = get_wsgi_application()

# Seed database on startup if empty
try:
    django.setup()
    from apps.models import User
    if not User.objects.filter(user_type='admin').exists():
        import subprocess
        import sys
        print("[LifeLink] Database is empty, seeding demo data...")
        result = subprocess.run([sys.executable, os.path.join(os.path.dirname(__file__), '..', 'seed_data.py')],
                              capture_output=True, text=True)
        if result.returncode == 0:
            print("[LifeLink] Seeding completed successfully!")
        else:
            print(f"[LifeLink] Seeding warning: {result.stderr}")
except Exception as e:
    print(f"[LifeLink] Seeding check skipped: {type(e).__name__}")
