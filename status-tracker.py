class Character:
    def __init__(self, name, player, st, hp_adjust, ht, fp_adjust, iq, will_adjust, er):
        hp = st+hp_adjust
        fp = ht+fp_adjust
        will = iq+will_adjust
        self.name=name
        self.player=player
        self.st=st
        self.ht=ht
        self.fp=fp
        self.hp=hp
        self.iq=iq
        self.will=will
        self.fp_adjust=fp_adjust
        self.hp_adjust=hp_adjust
        self.will_adjust=will_adjust
        self.er=er
        
        self.limb = hp/2
        self.extremity = hp/3
        self.major = hp/2
        self.knockback = st - 2
        
        self.init_fp_ranges(fp)
        self.init_hp_ranges(hp)
        self.init_will_ranges(will)

    def init_will_ranges(self, will):
        self.will_ok = (will, (2*will)/3)
        self.will_mild =((2*will)/3, will/3)
        self.will_major = (will/3, 0)
        self.will_constant = (0, -will)
        self.will_ranges = [
            self.will_ok,
            self.will_mild,
            self.will_major,
            self.will_constant
        ]

    def init_fp_ranges(self, fp):
        self.fp_ok = (fp, fp/3)
        self.fp_reeling = (fp/3, 0)
        self.fp_ko = (0, (fp*-1)-1)
        self.fp_ranges = [
            self.fp_ok,
            self.fp_reeling,
            self.fp_ko
        ]
        
    def init_hp_ranges(self, hp):
        self.hp_ranges = []
        ok_hp = (hp, 3)
        reeling_hp = (3, 0)
        
        self.hp_ranges.append(ok_hp)
        self.hp_ranges.append(reeling_hp)
        
        for i in range(0, 5):
            top = -1*i*hp
            bottom = -1*(i+1)*hp
            self.hp_ranges.append((top, bottom))
        self.hp_ranges.append((-5*hp, -10*hp))
        
colormode(RGB, 255)

class Colors:
    WHITE = color(255, 255, 255)
    BLACK = color(0, 0, 0)
    RED = color(255, 0, 0)
    D_GREEN = color(0, 100, 0)
    L_PURPLE = color(147, 112, 219)
    PURPLE = color(102,  51, 153)
    INDIGO = color(75, 0, 130)
    GREEN = color(0, 128, 0)
    AMBER = color(184, 134, 11)
    GOLD = color(255, 215,0)
    YELLOW = color(255, 255, 0)
    ORANGE = color(255, 165, 0)
    CRIMSON = color(220, 20, 60)
    VD_BLUE = color(25,  25, 112)
    D_BLUE = color(00, 00, 128)
    M_BLUE = color(230, 230, 139)
    L_BLUE = color(00, 00, 205)
    VL_BLUE = color(65, 105, 225)
    VVL_BLUE = color(135, 206, 250)

MSG=lambda c, v: "- Roll vs HT-%s or KO each turn\n- At %s, Roll vs HT or Die" % (c, v)

HP_EFFECTS = [
       (Colors.GREEN, lambda c, v: "- OK"),
       (Colors.AMBER, lambda c, v: "- Halve BS, Move, and Dodge"),
       (Colors.GOLD, lambda c, v: "- Roll vs HT or KO each turn"),
       (Colors.YELLOW, MSG),
       (Colors.ORANGE, MSG),
       (Colors.CRIMSON, MSG),
       (Colors.RED, MSG),
       (Colors.BLACK, lambda c, v: "- At %s, Instant Death\n- At %s, Body destroyed" % (v, 2*v), Colors.WHITE)
       ]

FP_EFFECTS = [
    (Colors.L_PURPLE, lambda c, v: "+FP to 1/3: OK", Colors.WHITE),
    (Colors.PURPLE, lambda c, v: "1/3 to 1: Very Tired -\nHalve MS, BS, ST", Colors.WHITE),
    (Colors.INDIGO, lambda c, v: "0 to -FP: Verge of Collapse -\nRoll vs HT or KO", Colors.WHITE)
]

WILL_EFFECTS = [
    (Colors.VL_BLUE, lambda c, v: "+Will to 2/3:\n-0 / No Change / None", Colors.WHITE),
    (Colors.L_BLUE, lambda c, v: "2/3 to 1/3:\n-2 / One Level Harder / Mild", Colors.WHITE),
    (Colors.D_BLUE, lambda c, v: "1/3 to 1:\n-5 / Autofail / Major", Colors.WHITE),
    (Colors.VD_BLUE, lambda c, v: "0 to -Will:\n-7 / Compulsion / Constant", Colors.WHITE)
]

BORDER_H=10
BORDER_W=10
GUTTER=10
STAT_W=200
STAT_H=70
STAT_FONT_SIZE=20
TRACK_HEADER_FONT_SIZE=16
TEXT_FONT_SIZE=12
BUBBLE_FONT_SIZE=8
FONT="Arial"
FONT_BOLD="Arial Bold"

TRACKER_Y = BORDER_H + STAT_H + GUTTER
HEADER_H = 45
BUBBLE_SIZE = 18
BUBBLE_OFFSET = BUBBLE_SIZE+2
TRACKER_H = 9 * HEADER_H

HP_TRACKER_W = 2.5*STAT_W 
P_WIDTH = 2*GUTTER + 2*BORDER_W + 2*STAT_W + HP_TRACKER_W
NAME_W = P_WIDTH - (STAT_W*2 + GUTTER*2 + BORDER_W*2)
P_HEIGHT = GUTTER + 2*BORDER_H + TRACKER_H + STAT_H
HP_X = BORDER_W
HP_Y = TRACKER_Y
FP_X = HP_TRACKER_W + GUTTER + BORDER_W
FP_Y = TRACKER_Y
INFO_X = P_WIDTH-STAT_W-BORDER_W
WILL_X = FP_X
WILL_Y = TRACKER_Y + HEADER_H * 4
FP_WIDTH = WIDTH - HP_TRACKER_W - (3 * GUTTER)

size(P_WIDTH, P_HEIGHT)

def draw_hp(char):
    fontsize(STAT_FONT_SIZE)
    stroke(Colors.D_GREEN)
    fill(Colors.D_GREEN)
    rect_x = BORDER_W
    rect_y = BORDER_H
    rect(rect_x, rect_y, STAT_W, STAT_H)
    stroke(Colors.INDIGO)
    txt = "ST:\t%s\nHP:\t%s" % (char.st, char.hp)
    txt_w, txt_h = textmetrics(txt)
    fill(Colors.WHITE)
    font(FONT_BOLD)
    text(txt, rect_x+GUTTER, rect_y+txt_h/2)

def draw_energy_reserve(char):
    if (char.er > 0):
        x = FP_X
        y = TRACKER_Y + 8 * HEADER_H
        w = STAT_W
        h = HEADER_H
        fill(Colors.M_BLUE)
        rect(x, y, w, h)
        txt = "Energy Reserve"
        txt_w, txt_h = textmetrics(txt)
        fontsize(TRACK_HEADER_FONT_SIZE)
        align(CENTER)
        fill(Colors.BLACK)
        text(txt, x, y+txt_h+4, w)
        b_y = y + HEADER_H / 2
        b_x = x + STAT_W/2 - (BUBBLE_OFFSET * char.er)/2
        for i in range(char.er, 0, -1):
            draw_bubble(i, b_x, b_y)
            b_x = b_x + BUBBLE_OFFSET
            
def draw_fp(char):
    fontsize(STAT_FONT_SIZE)
    stroke(Colors.INDIGO)
    fill(Colors.INDIGO)
    rect_x = FP_X
    rect_y = BORDER_H
    rect(rect_x, rect_y, STAT_W, STAT_H)
    txt = "HT:\t%s\nFP:\t%s" % (char.ht, char.fp)
    txt_w, txt_h = textmetrics(txt)
    fill(Colors.WHITE)
    font(FONT_BOLD)
    text(txt, rect_x+GUTTER, rect_y+txt_h/2)

def draw_name(char):
    fontsize(STAT_FONT_SIZE)
    stroke(Colors.BLACK)
    fill(Colors.WHITE)
    rect_x = STAT_W+BORDER_W+GUTTER
    rect_y = BORDER_H
    w = NAME_W
    h = STAT_H
    rect(rect_x, rect_y, w, h)
    fill(Colors.BLACK)
    txt = "Character:\t%s\nPlayer:\t\t%s" % (char.name, char.player)
    txt_w, txt_h = textmetrics(txt)
    font(FONT_BOLD)
    text(txt, rect_x+GUTTER, rect_y+txt_h/2)

def fp_col(index, x_0, y_0, txt, hue, txt_hue, range, offset_range):
    c_x_delta = (STAT_W / 3)
    c_w = c_x_delta + 1
    c_x = x_0 + c_x_delta * index
    
    # header
    fill(hue)
    stroke(Colors.BLACK)
    rect(c_x, y_0, c_w, HEADER_H)

    # column
    fill(hue)
    stroke(Colors.BLACK)
    rect(c_x, y_0+HEADER_H, c_w, TRACKER_H - 2 * HEADER_H)

    # header text
    font(FONT_BOLD)
    fontsize(TEXT_FONT_SIZE)
    txt_w, txt_h = textmetrics(txt)
    txt_y = y_0 + 0.5 * HEADER_H - txt_h/5
    fill(txt_hue)
    align(CENTER)
    text(txt, c_x, txt_y, c_w)
        
    # bubbles
    stroke(Colors.BLACK)
    fill(Colors.WHITE)
    b_x = c_x + c_x_delta/2 - BUBBLE_SIZE/2
    b_y_0 = y_0 + HEADER_H + BUBBLE_SIZE/2
    b_y = b_y_0
    for i in offset_range:
        b_y = b_y + (BUBBLE_OFFSET)        
        
    for i in range:
        draw_bubble(i, b_x, b_y)
        b_y = b_y + (BUBBLE_SIZE+2)  
        
def draw_bubble(i, b_x, b_y, hue = Colors.WHITE, txt_hue = Colors.BLACK):
    fontsize(BUBBLE_FONT_SIZE)
    b_txt_w, b_txt_h = textmetrics(i)
    stroke(Colors.BLACK)
    fill(hue)
    oval(b_x, b_y, BUBBLE_SIZE, BUBBLE_SIZE)
    fill(txt_hue)
    align(CENTER)
    font(FONT)
    text(i, b_x-BUBBLE_SIZE/2, b_y+3*BUBBLE_SIZE/4, 2*BUBBLE_SIZE)
          
    
def draw_fp_track_vertical(char):
    rect_x = FP_X
    rect_y = TRACKER_Y
    w = STAT_W
    h = TRACKER_H
    rect(rect_x, rect_y, w, h)
    # header
    fill(Colors.INDIGO)
    rect(rect_x, rect_y, w, HEADER_H)
    fontsize(TRACK_HEADER_FONT_SIZE)
    draw_track_header("Fatigue (FP) Tracking", rect_x, rect_y, w, Colors.INDIGO)

    col_y = rect_y + HEADER_H
    # columns
    fp_col(0, rect_x, col_y, "+FP to\n1/3", Colors.L_PURPLE, Colors.WHITE, char.fp_ok, range(0))
    fp_col(1, rect_x, col_y, "1/3 to 1", Colors.PURPLE, Colors.WHITE, char.fp_reeling, char.fp_ok)
    fp_col(2, rect_x, col_y, "0 to -FP", Colors.INDIGO, Colors.WHITE, char.fp_ko, range(0))

    draw_energy_reserve(char)

def draw_fp_track_horizontal(char):
    draw_track(FP_X, FP_Y, FP_WIDTH , 0.5, FP_EFFECTS, char.fp_ranges, "Fatigue (FP) Tracking", "Effects", False, Colors.INDIGO, "unconscious.png")

def draw_will_track_horizontal(char):
    draw_track(FP_X, WILL_Y, FP_WIDTH, 0.5, WILL_EFFECTS, char.will_ranges, "Will Tracking", "Will / SC / Interference", False, Colors.VD_BLUE, "supernatural.png")

def draw_hp_track(char):
    draw_track(HP_X, HP_Y, HP_TRACKER_W, 0.6, HP_EFFECTS, char.hp_ranges, "Hit Point (HP) Tracking", "Effects", True, Colors.D_GREEN, "skull.jpg")
    
def draw_track(start_x, start_y, width, col_1_ratio, effects_list, stat_ranges, header, effects, offset_second_row, header_color, img):
    x = start_x
    y = start_y
    w = width
    h = TRACKER_H
    col_1_w = w * col_1_ratio
    col_2_w = width - col_1_w
    col_1_x = x
    col_2_x = col_1_x + col_1_w
    rect(x, y, w, h)

    # headers
    draw_track_header(header, col_1_x, y, col_1_w, header_color)
    draw_track_header(effects, col_2_x, y, col_2_w, header_color)
    
    # rows
    fontsize(TEXT_FONT_SIZE)
    for i, row in enumerate(effects_list):
        y_c = y + HEADER_H * (i + 1)
        stat_range = stat_ranges[i]
        bottom = stat_range[0]
        top = stat_range[1]

        fill(row[0])
        # col 1
        rect(col_1_x, y_c, col_1_w, HEADER_H)
        
        # col 2
        rect(col_2_x, y_c, col_2_w, HEADER_H)
        try: txt_hue = effects_list[i][2] 
        except: txt_hue = Colors.BLACK
        txt_func=row[1]
        txt = txt_func(i-2, bottom)
        txt_w, txt_h = textmetrics(txt)
        fill(txt_hue)
        fontsize(TEXT_FONT_SIZE)
        font(FONT)
        text(txt, col_2_x+GUTTER, y_c + HEADER_H/2)
        
        b_y = y_c + HEADER_H / 2 - BUBBLE_SIZE/2
        # bubbles
        if (i == 1 and offset_second_row):
            offset = b_x + BUBBLE_OFFSET
        else:
            offset = x
        if (i < len(effects_list) - 1):
            for b, value in enumerate(range(stat_range[0], stat_range[1], -1)):
                b_x = offset + BUBBLE_OFFSET * b
                if (i > 2 and b == 0):
                    draw_bubble(value, b_x, b_y, Colors.BLACK, Colors.WHITE)
                else:
                    draw_bubble(value, b_x, b_y, Colors.WHITE, Colors.BLACK)
        else:
            image(img, offset + BUBBLE_SIZE/2, y_c+BUBBLE_SIZE/3, HEADER_H * 0.75, HEADER_H * 0.75)

def draw_track_header(txt, x0, y0, w, hue):
    fill(hue)
    rect(x0, y0, w, HEADER_H)    
    fontsize(TRACK_HEADER_FONT_SIZE)
    align(CENTER)
    head_w, head_h = textmetrics(txt)
    font(FONT_BOLD)
    fill(Colors.WHITE)
    text(txt, x0, y0+HEADER_H-head_h, w)

def draw_info_msg(i, x0, y0, col_1_txt, col_2_txt, hue=Colors.WHITE, txt_hue=Colors.BLACK):
    font(FONT)
    fontsize(TEXT_FONT_SIZE)
    y = y0 + HEADER_H * i
    fill(hue)
    rect(x0, y, STAT_W, HEADER_H)
    
    # col 2
    fill(txt_hue)
    col_1_w=STAT_W/3
    col_1_txt_x = x0 + GUTTER
    col_1_txt_y = valign_middle(y, HEADER_H, col_1_txt)
    font(FONT)
    text(col_1_txt, col_1_txt_x, col_1_txt_y)
    
    # col 2
    col_2_x = x0 + col_1_w
    fill(hue)
    rect(col_2_x, y, STAT_W-col_1_w, HEADER_H)
    fill(txt_hue)
    col_2_txt_y = valign_middle(y, HEADER_H, col_2_txt)
    font(FONT)
    text(col_2_txt, col_2_x+GUTTER, col_2_txt_y)

def valign_middle(y0, h, txt):
    txt_w, txt_h = textmetrics(txt)
    return y0 + h/2# + txt_h/4

def draw_info(char):
    fill(Colors.WHITE)
    stroke(Colors.BLACK)
    x = INFO_X
    y = TRACKER_Y
    w = STAT_W
    h = TRACKER_H
    rect(x, y, w, h)
    
    draw_track_header("Fatigue Effects", x, y, w, Colors.INDIGO)
    draw_info_msg(1, x, y, "+FP to\n1/3", "OK", Colors.L_PURPLE, Colors.WHITE)
    draw_info_msg(2, x, y, "1/3 to 1", "Very Tired:\nHalve MS, BS, ST", Colors.PURPLE, Colors.WHITE)
    draw_info_msg(3, x, y, "0 to -FP", "Verge of Collapse:\nRoll vs HT or KO", Colors.INDIGO, Colors.WHITE)

    draw_track_header("Injury & Wounds", x, y+HEADER_H*4, w, Colors.D_GREEN)
    draw_info_msg(5, x, y, "%s pts" % char.limb, "Verge of Collapse:\nRoll vs HT or KO")
    draw_info_msg(6, x, y, "%s pts" % char.extremity, "Verge of Collapse:\nRoll vs HT or KO")
    draw_info_msg(7, x, y, "%s pts" % char.major, "Verge of Collapse:\nRoll vs HT or KO")
    draw_info_msg(8, x, y, "%s pts" % char.knockback, "Verge of Collapse:\nRoll vs HT or KO")
      
def draw_layout(char):
    draw_hp(char)
    draw_name(char)
    draw_fp(char)
    draw_hp_track(char)
    draw_fp_track_horizontal(char)
    draw_will_track_horizontal(char)
    # draw_info(char)

# character = Character("RC Cleveland", "Marc Faletti", 10, 1, 11, 1, 14, 1, 0)
# character = Character("Natalia Satsuki", "Amanda Marcotte", 10, 0, 10, 0, 13, -1, 0)
# character = Character("Nora Blackburn", "Tinsley Webster", 10, 1, 12, -1, 14, 0, 0)
character = Character("Everett O'Connel", "Stewart McMacken", 12, 0, 12, 0, 15, -5, 0)

draw_layout(character)
    
