#!/bin/bash
git reset --hard HEAD
git pull
cd ../saves
mv Test.save ../temp/Test.save
rm *.save
mv ../temp/Test.save Test.save

