class Character:
    def __init__(self, name, player, st, hp_adjust, ht, fp_adjust, er):
        hp = st+hp_adjust
        fp = ht+fp_adjust
        self.name=name
        self.player=player
        self.st=st
        self.ht=ht
        self.fp=fp
        self.hp=hp
        self.fp_adjust=fp_adjust
        self.hp_adjust=hp_adjust
        self.er=er
        
        self.fp_ok = range(fp, fp/3, -1)
        self.fp_reeling = range(fp/3, 0, -1)
        self.fp_ko = range(0, (fp*-1)-1, -1)

        self. init_hp_ranges(hp)
                
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

MSG=lambda c, v: "- Roll vs HT-%s or KO each turn\n- At %s, Roll vs HT or Die" % (c, v)
KOS = [
       (Colors.GREEN, lambda c, v: "- OK"),
       (Colors.AMBER, lambda c, v: "- Halve BS, Move, and Dodge"),
       (Colors.GOLD, lambda c, v: "- Roll vs HT or KO each turn"),
       (Colors.YELLOW, MSG),
       (Colors.ORANGE, MSG),
       (Colors.CRIMSON, MSG),
       (Colors.RED, MSG),
       (Colors.BLACK, lambda c, v: "- At %s, Instant Death\n- At %s, Body destroyed" % (v, 2*v), Colors.WHITE)
       ]

BORDER_H=10
BORDER_W=10
GUTTER=10
STAT_W=200
STAT_H=75
WIDTH=800
STAT_FONT_SIZE=20
TRACK_HEADER_FONT_SIZE=16
TEXT_FONT_SIZE=12
BUBBLE_FONT_SIZE=8
    
FP_X = WIDTH-STAT_W-BORDER_W
TRACKER_Y = BORDER_H + STAT_H + GUTTER
HEADER_H = 50
BUBBLE_SIZE = 18
BUBBLE_OFFSET = BUBBLE_SIZE+2
TRACKER_H = 9 * HEADER_H

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
    text(txt, rect_x+GUTTER, rect_y+txt_h/2)

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
    text(txt, rect_x+GUTTER, rect_y+txt_h/2)

def draw_name(char):
    fontsize(STAT_FONT_SIZE)
    stroke(Colors.BLACK)
    fill(Colors.WHITE)
    rect_x = STAT_W+BORDER_W+GUTTER
    rect_y = BORDER_H
    w = WIDTH-(STAT_W*2+GUTTER*2+BORDER_W*2)
    h = STAT_H
    rect(rect_x, rect_y, w, h)
    fill(Colors.BLACK)
    txt = "Character:\t%s\nPlayer:\t\t%s" % (char.name, char.player)
    txt_w, txt_h = textmetrics(txt)
    text(txt, rect_x+GUTTER, rect_y+txt_h/2)
    
def draw_hp_track(char):
    pass

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
    b_y_0 = y_0 + HEADER_H + BUBBLE_SIZE
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
    text(i, b_x-BUBBLE_SIZE/2, b_y+3*BUBBLE_SIZE/4, 2*BUBBLE_SIZE)
          
    
def draw_fp_track(char):
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

def draw_hp_track(char):
    x = BORDER_W
    y = TRACKER_Y
    w = WIDTH - STAT_W - 2 * BORDER_W - GUTTER
    h = TRACKER_H
    col_1_w = 2 * w / 3
    col_2_w = w / 3
    col_1_x = x
    col_2_x = col_1_x + col_1_w
    rect(x, y, w, h)

    # headers
    draw_track_header("Hit Point (HP) Tracking", col_1_x, y, col_1_w, Colors.D_GREEN)
    draw_track_header("Effects", col_2_x, y, col_2_w, Colors.D_GREEN)
    
    # rows
    fontsize(TEXT_FONT_SIZE)
    for i, row in enumerate(KOS):
        y_c = y + HEADER_H * (i + 1)
        hp_range = char.hp_ranges[i]
        bottom = hp_range[0]
        top = hp_range[1]

        fill(row[0])
        # col 1
        rect(col_1_x, y_c, col_1_w, HEADER_H)
        
        # col 2
        rect(col_2_x, y_c, col_2_w, HEADER_H)
        try: txt_hue = KOS[i][2] 
        except: txt_hue = Colors.BLACK
        txt_func=row[1]
        txt = txt_func(i-2, bottom)
        txt_w, txt_h = textmetrics(txt)
        fill(txt_hue)
        fontsize(TEXT_FONT_SIZE)
        text(txt, col_2_x+BUBBLE_SIZE, y_c + HEADER_H/2)
        
        b_y = y_c + HEADER_H / 2 - BUBBLE_SIZE/2
        # bubbles
        if (i == 1):
            offset = b_x
        else:
            offset = x
        if (i < len(KOS) - 1):
            for i, value in enumerate(range(hp_range[0], hp_range[1], -1)):
                b_x = offset + BUBBLE_SIZE + BUBBLE_OFFSET * i
                draw_bubble(value, b_x, b_y)
        else:
            image("skull.jpg", offset + BUBBLE_SIZE/2, y_c+BUBBLE_SIZE/3, HEADER_H * 0.75, HEADER_H * 0.75)

def draw_track_header(txt, x0, y0, w, hue):
    fill(hue)
    rect(x0, y0, w, HEADER_H)    
    fontsize(TRACK_HEADER_FONT_SIZE)
    align(CENTER)
    head_w, head_h = textmetrics(txt)
    fill(Colors.WHITE)
    text(txt, x0, y0+HEADER_H-head_h, w)
    
def draw_layout(char):
    draw_hp(char)
    draw_name(char)
    draw_fp(char)
    draw_hp_track(char)
    draw_fp_track(char)

character = Character("Scrape", "Tinsley Webster", 11, 0, 12, 0, 5)

draw_layout(character)
    
