/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include "filters.h"

void JNIFUNCF(ImageFilterExposure, nativeApplyFilter, jobject bitmap, jint width, jint height, jfloat bright)
{
    char* destination = 0;
    AndroidBitmap_lockPixels(env, bitmap, (void**) &destination);
    unsigned char * rgb = (unsigned char * )destination;
    int i;
    int len = width * height * 4;

    int m =   (255-bright);

    for (i = 0; i < len; i+=4)
    {
        rgb[RED]   = clamp((255*(rgb[RED]))/m);
        rgb[GREEN] = clamp((255*(rgb[GREEN]))/m);
        rgb[BLUE]  = clamp((255*(rgb[BLUE]))/m);
    }
    AndroidBitmap_unlockPixels(env, bitmap);
}

