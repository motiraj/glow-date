import os
from PIL import Image, ImageDraw, ImageFont

def generate_graphic():
    width = 1024
    height = 500

    # 1. Create base background image with rich Vedic Warm Gradient
    # Gradient from deep warm saffron/burgundy (#4A1504) to rich amber gold (#9E4A06) to warm temple gold (#C26D10)
    img = Image.new('RGB', (width, height), '#4A1504')
    draw = ImageDraw.Draw(img)

    for x in range(width):
        r = int(74 + (194 - 74) * (x / width))
        g = int(21 + (109 - 21) * (x / width))
        b = int(4 + (16 - 4) * (x / width))
        draw.line([(x, 0), (x, height)], fill=(r, g, b))

    # Add subtle radial-like top-bottom shading
    overlay = Image.new('RGBA', (width, height), (0, 0, 0, 0))
    overlay_draw = ImageDraw.Draw(overlay)
    for y in range(height):
        alpha = int(40 * abs(y - height/2) / (height/2))
        overlay_draw.line([(0, y), (width, y)], fill=(20, 5, 0, alpha))
    img.paste(overlay, (0, 0), overlay)

    # 2. Draw subtle decorative mandala / celestial circles in the background
    circle_overlay = Image.new('RGBA', (width, height), (0, 0, 0, 0))
    circle_draw = ImageDraw.Draw(circle_overlay)

    # Big decorative celestial ring on right side
    cx, cy = 820, 250
    for rad in [140, 180, 220, 260, 300, 340]:
        circle_draw.ellipse([cx - rad, cy - rad, cx + rad, cy + rad], outline=(255, 230, 180, 22), width=2)

    # 3. Load App Logo
    logo_path = "app/src/main/res/drawable/app_logo.png"
    if os.path.exists(logo_path):
        logo = Image.open(logo_path).convert('RGBA')
        logo_size = 210
        logo = logo.resize((logo_size, logo_size), Image.Resampling.LANCZOS)

        # Place logo on the left
        logo_x = 75
        logo_y = (height - logo_size) // 2 - 10

        # Draw glowing backdrop behind logo
        glow_pad = 20
        circle_draw.ellipse(
            [logo_x - glow_pad, logo_y - glow_pad, logo_x + logo_size + glow_pad, logo_y + logo_size + glow_pad],
            fill=(255, 215, 0, 35)
        )
        img.paste(circle_overlay, (0, 0), circle_overlay)
        img.paste(logo, (logo_x, logo_y), logo)
    else:
        img.paste(circle_overlay, (0, 0), circle_overlay)

    # 4. Draw Typography
    text_draw = ImageDraw.Draw(img)

    # Fonts
    font_bold = None
    font_sub = None
    font_tag = None
    font_nepali = None

    # Search for system fonts
    font_candidates = [
        "C:/Windows/Fonts/seguiemj.ttf",
        "C:/Windows/Fonts/NirmalaB.ttf",
        "C:/Windows/Fonts/Nirmala.ttf",
        "C:/Windows/Fonts/segoeuib.ttf",
        "C:/Windows/Fonts/arialbd.ttf"
    ]

    nirmala_b = "C:/Windows/Fonts/NirmalaB.ttf"
    nirmala_reg = "C:/Windows/Fonts/Nirmala.ttf"
    segoe_b = "C:/Windows/Fonts/segoeuib.ttf"

    title_font = ImageFont.truetype(nirmala_b, 62) if os.path.exists(nirmala_b) else ImageFont.load_default()
    subtitle_font = ImageFont.truetype(nirmala_b, 30) if os.path.exists(nirmala_b) else ImageFont.load_default()
    badge_font = ImageFont.truetype(nirmala_b, 20) if os.path.exists(nirmala_b) else ImageFont.load_default()
    tagline_font = ImageFont.truetype(nirmala_reg, 20) if os.path.exists(nirmala_reg) else ImageFont.load_default()

    text_x = 325

    # Badge: "वि.सं. २०८३ अद्यावधिक • 100% OFFLINE & ACCURATE"
    badge_text = " ★ वि.सं. २०८३ अद्यावधिक  •  वैदिक पञ्चाङ्ग तथा कुण्डली ★ "
    badge_y = 75
    bbox = text_draw.textbbox((text_x, badge_y), badge_text, font=badge_font)
    padding_x, padding_y = 14, 6
    text_draw.rounded_rectangle(
        [bbox[0] - padding_x, bbox[1] - padding_y, bbox[2] + padding_x, bbox[3] + padding_y],
        radius=8,
        fill=(138, 20, 0, 220),
        outline=(255, 220, 130, 255),
        width=1
    )
    text_draw.text((text_x, badge_y), badge_text, fill=(255, 235, 170), font=badge_font)

    # Main Title: Glow Date (ग्लोडेट)
    title_y = 120
    # Shadow
    text_draw.text((text_x + 3, title_y + 3), "Glow Date  ग्लोडेट", fill=(30, 5, 0), font=title_font)
    # Bright Gold / White
    text_draw.text((text_x, title_y), "Glow Date  ग्लोडेट", fill=(255, 255, 255), font=title_font)

    # Subtitle: नेपाली पात्रो • राशिफल • पञ्चाङ्ग
    sub_y = 210
    text_draw.text((text_x + 2, sub_y + 2), "नेपाली पात्रो  •  पञ्चाङ्ग  •  राशिफल  •  मिति रूपान्तरण", fill=(40, 10, 0), font=subtitle_font)
    text_draw.text((text_x, sub_y), "नेपाली पात्रो  •  पञ्चाङ्ग  •  राशिफल  •  मिति रूपान्तरण", fill=(255, 223, 110), font=subtitle_font)

    # Bullet / Feature Points
    features = [
        "✔  वि.सं. २०८३ सालका सम्पूर्ण चाडपर्व तथा शुभ साइतहरू",
        "✔  प्रत्यक्ष सूर्योदय-सूर्यास्त, चौघडिया, होरा र घडी-पला",
        "✔  जन्म कुण्डली (D-1 Lagna), विंशोत्तरी दशा र सूक्ष्म गोचर",
        "✔  द्रुत BS ↔ AD मिति रूपान्तरण र उमेर क्याल्कुलेटर"
    ]

    start_y = 275
    line_gap = 42
    for i, feat in enumerate(features):
        fy = start_y + (i * line_gap)
        # draw checkmark icon background pill
        text_draw.text((text_x + 1, fy + 1), feat, fill=(35, 8, 0), font=tagline_font)
        text_draw.text((text_x, fy), feat, fill=(250, 245, 235), font=tagline_font)

    # Bottom bar accent
    bar_overlay = Image.new('RGBA', (width, height), (0, 0, 0, 0))
    bar_draw = ImageDraw.Draw(bar_overlay)
    bar_draw.rectangle([0, height - 10, width, height], fill=(255, 200, 50, 220))
    img.paste(bar_overlay, (0, 0), bar_overlay)

    output_path = "feature_graphic.png"
    img.save(output_path, "PNG", optimize=True)
    print(f"Feature Graphic generated successfully at: {os.path.abspath(output_path)}")
    print(f"Dimensions: {img.size[0]} x {img.size[1]} px")
    print(f"File size: {os.path.getsize(output_path) / 1024:.2f} KB")

if __name__ == "__main__":
    generate_graphic()
