/****************************************************************************
 Author: Luma (stubma@gmail.com)
 
 https://github.com/stubma/cocos2dx-classical
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/
#include "CCTMXLayerInfo.h"

NS_CC_BEGIN

CCTMXLayerInfo::CCTMXLayerInfo() :
m_tiles(NULL),
m_layerHeight(0),
m_layerWidth(0),
m_alpha(255),
m_visible(true),
m_offsetX(0),
m_offsetY(0) {
}

CCTMXLayerInfo::~CCTMXLayerInfo() {
	CC_SAFE_FREE(m_tiles);
}

CCTMXLayerInfo* CCTMXLayerInfo::create() {
	CCTMXLayerInfo* li = new CCTMXLayerInfo();
	CC_SAFE_AUTORELEASE_RETURN(li, CCTMXLayerInfo*);
}

void CCTMXLayerInfo::addProperty(const string& key, const string& value) {
	if(!m_properties.objectForKey(key)) {
		m_properties.setObject(CCString::create(value), key);
	}
}

string CCTMXLayerInfo::getProperty(const string& key) {
	CCString* p = (CCString*)m_properties.objectForKey(key);
	if(p)
		return p->getCString();
	else
		return "";
}

int CCTMXLayerInfo::getTileAt(int index) {
    return m_tiles[index];
}

NS_CC_END