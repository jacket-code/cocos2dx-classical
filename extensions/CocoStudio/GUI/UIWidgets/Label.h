/****************************************************************************
 Copyright (c) 2013 cocos2d-x.org
 
 http://www.cocos2d-x.org
 
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

#ifndef __UILABEL_H__
#define __UILABEL_H__

#include "../BaseClasses/UIWidget.h"

NS_CC_BEGIN

namespace ui {

/**
*   @js NA
*   @lua NA
*/
class CC_DLL Label : public Widget
{
    DECLARE_CLASS_GUI_INFO
    
public:
    /**
     * Default constructor
     */
    Label();
    
    /**
     * Default destructor
     */
    virtual ~Label();
    
    /**
     * Allocates and initializes.
     */
    static Label* create();
    
    // set dimension
    void setDimensions(const CCSize &dim);
    
    /**
     * Changes the string value of label.
     *
     * @param text  string value.
     */
    void setText(const std::string& text);
    
    // for interface compatibility
    void setString(const std::string& text);
    
    /**
     * Gets the string value of label.
     *
     * @return text  string value.
     */
    const char* getStringValue();
    
    // for interface compatibility
    const char* getString();
    const char* getText();
    
    /**
     * Gets the string length of label.
     *
     * @return  string length.
     */
    int getStringLength();
    
    /**
     * Sets the font size of label.
     *
     * @param  font size.
     */
    void setFontSize(int size);
    
    int getFontSize();
    
    /** enable or disable shadow for the label */
    void enableShadow(const CCSize &shadowOffset, unsigned int shadowColor, float shadowBlur);
    
    /** disable shadow rendering */
    void disableShadow();
    
    /** enable or disable stroke */
    void enableStroke(const ccColor3B &strokeColor, float strokeSize);
    
    /** disable stroke */
    void disableStroke();
    
    /**
     * Sets the font name of label.
     *
     * @param  font name.
     */
    void setFontName(const std::string& name);
    
    const char* getFontName();
    /**
     * Sets the touch scale enabled of label.
     *
     * @param  touch scale enabled of label.
     */
    void setTouchScaleChangeEnabled(bool enabled);
    
    /**
     * Gets the touch scale enabled of label.
     *
     * @return  touch scale enabled of label.
     */
    bool isTouchScaleChangeEnabled();
    
    //override "setAnchorPoint" method of widget.
    virtual void setAnchorPoint(const CCPoint &pt);
    
    //override "getContentSize" method of widget.
    virtual const CCSize& getContentSize() const;
    
    //override "getVirtualRenderer" method of widget.
    virtual CCNode* getVirtualRenderer();
    
    /**
     * Returns the "class name" of widget.
     */
    virtual std::string getDescription() const;
    
    void setTextAreaSize(const CCSize &size);
    
    CCSize getTextAreaSize();
    
    void setTextHorizontalAlignment(CCTextAlignment alignment);
    
    CCTextAlignment getTextHorizontalAlignment();
    
    void setTextVerticalAlignment(CCVerticalTextAlignment alignment);
    
    CCVerticalTextAlignment getTextVerticalAlignment();
    
    void setGlobalImageScaleFactor(float scale, bool mustUpdateTexture = true);
    float getGlobalImageScaleFactor();
    
protected:
    virtual bool init();
    virtual void initRenderer();
    virtual void onPressStateChangedToNormal();
    virtual void onPressStateChangedToPressed();
    virtual void onPressStateChangedToDisabled();
    virtual void onSizeChanged();
    virtual void updateTextureColor();
    virtual void updateTextureOpacity();
    virtual void updateTextureRGBA();
    virtual void updateFlippedX();
    virtual void updateFlippedY();
    void labelScaleChangedWithSize();
    virtual Widget* createCloneInstance();
    virtual void copySpecialProperties(Widget* model);
protected:
    bool _touchScaleChangeEnabled;
    float _normalScaleValueX;
    float _normalScaleValueY;
    std::string _fontName;
    int _fontSize;
    float _onSelectedScaleOffset;
    CCLabelTTF* _labelRenderer;
};

}

NS_CC_END

#endif /* defined(__CocoGUI__Label__) */
