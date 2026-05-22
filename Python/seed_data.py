#!/usr/bin/env python
"""
Seed basic demo data into the database
Run with: python seed_data.py
"""

import os
import django

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'project.settings')
django.setup()

from apps.models import User, SafeZones

def seed_demo_users():
    """Create demo accounts for testing"""
    demo_accounts = [
        {
            'name': 'Admin User',
            'mail': 'admin@crisis.com',
            'password': 'admin123',
            'user_type': 'admin',
            'mobile': '9876543210',
            'city': 'Hyderabad',
            'location': 'Admin Office'
        },
        {
            'name': 'NGO One',
            'mail': 'ngo1@crisis.com',
            'password': 'ngo123',
            'user_type': 'ngo',
            'mobile': '9876543211',
            'city': 'Hyderabad',
            'location': 'NGO Office'
        },
        {
            'name': 'Volunteer One',
            'mail': 'vol1@crisis.com',
            'password': 'vol123',
            'user_type': 'volunteer',
            'mobile': '9876543212',
            'city': 'Hyderabad',
            'location': 'Volunteer Center'
        },
        {
            'name': 'User One',
            'mail': 'user1@crisis.com',
            'password': 'user123',
            'user_type': 'user',
            'mobile': '9876543213',
            'city': 'Hyderabad',
            'location': 'Home'
        }
    ]

    for account in demo_accounts:
        user, created = User.objects.get_or_create(
            mail=account['mail'],
            defaults={
                'name': account['name'],
                'password': account['password'],
                'user_type': account['user_type'],
                'mobile': account['mobile'],
                'city': account['city'],
                'location': account['location']
            }
        )
        status = "[CREATED]" if created else "[EXISTS]"
        print(status + ": " + account['name'] + " (" + account['user_type'] + ") - " + account['mail'])


def seed_safe_zones():
    """Create sample safe zones in Hyderabad"""
    safe_zones = [
        {
            'title': 'Community Center - Gachibowli',
            'details': 'Community emergency shelter with 500 capacity',
            'LatLon': '17.3850,78.3967'
        },
        {
            'title': 'School Shelter - Madhapur',
            'details': 'School building converted to shelter with 300 capacity',
            'LatLon': '17.3596,78.3597'
        },
        {
            'title': 'Hospital Relief - KIMS',
            'details': 'Hospital emergency relief center with 200 capacity',
            'LatLon': '17.3842,78.4711'
        },
        {
            'title': 'Assembly Hall - Secunderabad',
            'details': 'Large assembly hall shelter with 400 capacity',
            'LatLon': '17.3632,78.5043'
        }
    ]

    for zone in safe_zones:
        safe_zone, created = SafeZones.objects.get_or_create(
            title=zone['title'],
            defaults={
                'details': zone['details'],
                'LatLon': zone['LatLon']
            }
        )
        status = "[CREATED]" if created else "[EXISTS]"
        print(status + ": " + zone['title'])


if __name__ == '__main__':
    print("\n=== Seeding Demo Data ===\n")

    print("1. Creating demo user accounts...")
    seed_demo_users()

    print("\n2. Creating safe zones...")
    seed_safe_zones()

    print("\n=== Seeding Complete! ===\n")
