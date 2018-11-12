#!/usr/bin/python
# -*- coding: UTF-8 -*-

import os
import shutil

for root, dirs, files in os.walk('.'):
    for name in dirs:
        if name == 'build':
            shutil.rmtree(root + os.sep + name)
            print root + os.sep + name
