from django.contrib import admin
from django.urls import path
from . import views

urlpatterns = [
    path('deleteSafeZone/',view=views.deleteSafeZone,name="deleteSafeZone"),
    path('viewOfType/',view=views.viewBothVolunteer,name="viewOfType"),
    path("register/", views.register, name="register"),
    path("login/", views.login, name="login"),
    path("addComplaint/", views.add_complaint, name="add_complaint"),
    path("getCountOf/", views.get_count_of, name="get_count_of"),
    path("getCities/", views.get_cities, name="get_cities"),
    path(
        "updateCityAndGetMessages/",
        views.updateCityAndGetMessages,
        name="updateCityAndGetMessages",
    ),
    path(
        "viewComplaintOfUsers/", views.viewComplaintOfUsers, name="viewComplaintOfUsers"
    ),
    path("getUserDetails/", views.get_user_details, name="getUserDetails"),
    path("sendMessage/", views.sendMessage, name="sendMessage"),
    path(
        "getOurCityComplaints/",
        views.get_unassigned_complaints_by_city,
        name="get_unassigned_complaints_by_city",
    ),
    path(
        "complaintGraphDetails/",
        views.complaintGraphDetails,
        name="complaintGraphDetails",
    ),
    path("updateComplaint/", views.updateComplaint, name="updateComplaint"),
    path(
        "updateComplaintVolunteer/",
        views.updateComplaintVolunteer,
        name="updateComplaintVolunteer",
    ),
    path("completedComplaint/", views.completedComplaint, name="completedComplaint"),
    path(
        "viewComplaintOfLatLon/",
        views.viewComplaintOfLatLon,
        name="viewComplaintOfLatLon",
    ),
    path("sendSos/", views.sendSos, name="sendSos"),
    path("getSosOFUserID/", views.getSosOFUserID, name="getSosOFUserID"),
    path("getWithUserDetails/", views.getWithUserDetails, name="getWithUserDetails"),
    path('addSafeZone/',view=views.addSafeZone,name="addSafeZone"),
    path('getSafeZones/',view=views.getSafeZones,name="getSafeZones"),
    path('add_complaint_with_View/',view=views.add_complaint_with_View,name="add_complaint_with_View"),
    path('getUploadedVideo/',view=views.getUploadedVideo,name="getUploadedVideo"),
    path('getUploadedWithUserDetails/',view=views.getUploadedWithUserDetails,name="getUploadedWithUserDetails"),
    path('getUploadedWithUserDetails/',view=views.getUploadedWithUserDetails,name="getUploadedWithUserDetails"),
    path('feedBack/',view=views.updateAComplaintWithFeeBack,name='feedBack'),
    path('aiCrisisAgent/', view=views.aiCrisisAgent, name='aiCrisisAgent'),
]

