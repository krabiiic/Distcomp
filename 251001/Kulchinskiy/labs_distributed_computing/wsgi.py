"""
WSGI config for labs_distributed_computing project.

It exposes the WSGI callable as a module-level variable named ``publisher``.

For more information on this file, see
https://docs.djangoproject.com/en/5.1/howto/deployment/wsgi/
"""

import os

from django.core.wsgi import get_wsgi_application

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'labs_distributed_computing.settings')

application = get_wsgi_application()
