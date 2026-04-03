# 🦇 BAT ESCAPE

<div align="center">

### Navigate the Deadly Cave • Collect Glowing Insects • Survive

![Java](https://img.shields.io/badge/Java-8+-orange?style=for-the-badge&logo=java)
![Swing](https://img.shields.io/badge/GUI-Swing-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

*A Flappy Bird-inspired cave adventure built with Java Swing*

[🎮 Play Now](#installation--running) • [📖 How to Play](#how-to-play) • [⚙️ Technical Details](#technical-highlights)

</div>

---

## 🎯 About

**Bat Escape** is a fast-paced, reflex-testing arcade game where you guide a bat through an endless dark cave filled with deadly obstacles. Inspired by Flappy Bird mechanics, this game combines precise controls, smooth physics, and addictive gameplay with beautiful hand-coded 2D graphics.

### 🌟 Game Stats

- ♾️ **Endless Gameplay** - Procedurally generated obstacles keep every run unique
- ✨ **+50 Bonus Points** - Collect glowing insects for massive score boosts
- ⚡ **60 FPS** - Silky-smooth gameplay powered by optimized Java Swing

---

## 🎮 Features

### 🦇 **Flappy Bird Mechanics**
Master the art of flight! Press or hold SPACE to make your bat flap through the treacherous cave. Precise timing is your key to survival.

### 🪨 **Dynamic Obstacles**
Navigate through randomly generated cave formations with spiked stalactites and stalagmites. Every game is a unique challenge!

### ✨ **Bonus Collectibles**
Catch glowing insects for massive bonus points! Risk versus reward gameplay adds strategic depth to every flight.

### 🎨 **Polished Visuals**
- Dark gradient cave backgrounds with atmospheric particle effects
- Smooth bat animations with flapping wings
- Glowing insect effects with dynamic lighting
- Custom-drawn obstacles with menacing spikes
- Professional UI with clean fonts and scoring system

---

## 🕹️ How to Play

| Key | Action | Description |
|-----|--------|-------------|
| `SPACE` | **Flap / Start** | Press or hold SPACE to make the bat flap upward. Release to let gravity pull you down. Master the rhythm! |
| `R` | **Restart** | Hit an obstacle? Press R to restart and try again. Every attempt makes you better! |
| `ESC` | **Exit** | Press ESC to exit the game anytime. |

### 🎯 Gameplay Tips
- **Start slow**: Get comfortable with the flap strength and gravity before rushing
- **Watch the rhythm**: The bat has a natural bobbing motion - use it to your advantage
- **Risk vs Reward**: Insects give 50 bonus points but are positioned in challenging spots
- **Stay centered**: Keep to the middle of gaps when possible for maximum reaction time

---

## 🚀 Installation & Running

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Terminal/Command Prompt access

### Quick Start

```bash
# Compile the game
javac BatEscape.java

# Run the game
java BatEscape
```

### One-Line Command
```bash
javac BatEscape.java && java BatEscape
```

---

## ⚙️ Technical Highlights

### 🎨 **Custom Graphics Engine**
Hand-coded 2D graphics using Java2D. Dark gradient backgrounds, glowing effects, and smooth animations create an immersive cave atmosphere.

### ⚡ **Real-time Physics Simulation**
- Gravity simulation with velocity damping
- Responsive flap mechanics with impulse-based movement
- Maximum fall speed capping for fair gameplay
- Smooth 60 FPS game loop using Swing Timer

### 🔄 **Efficient Object Management**
- Smart obstacle recycling system prevents memory bloat
- Off-screen objects are removed and repositioned
- Optimized collision detection using Rectangle intersection
- Performance optimized for endless gameplay sessions

### 🎯 **Precise Collision Detection**
Rectangle-based collision detection ensures fair and predictable gameplay. You always know exactly why you died!

### 🏗️ **Architecture**
```
BatEscape.java (Main entry point)
├── GameFrame (JFrame window)
├── GamePanel (Game loop, rendering, input)
├── Bat (Player character with physics)
├── Obstacle (Cave formations)
└── Insect (Bonus collectibles)
```

---

## 🎨 Game Mechanics Deep Dive

### Physics Constants
```java
GRAVITY = 0.45          // Downward acceleration per frame
FLAP_VY = -7.5         // Upward velocity on flap
SCROLL_SPEED = 4.2     // World scroll speed (pixels/frame)
FPS = 60               // Target framerate
```

### Scoring System
- **Time Score**: Increases every 3 frames while alive (~20 points/second)
- **Bonus Score**: +50 points per insect collected
- **Final Score**: Time Score + Bonus Score

### Difficulty Features
- Gap height: 170 pixels (tight but fair)
- Obstacle spacing: 260 pixels
- Random gap positioning keeps you alert
- Insect spawn chance: 55% per obstacle set

---

## 🛠️ Code Structure

```java
// Main game classes
public class BatEscape          // Entry point
class GameFrame extends JFrame  // Window management
class GamePanel extends JPanel  // Core game logic
class Bat                       // Player entity
class Obstacle                  // Cave obstacles
class Insect                    // Collectible bonuses
```

### Key Features in Code
- **Event-driven architecture** using KeyListener
- **Component-based rendering** with custom paintComponent
- **State management** for game flow (idle, running, game over)
- **Procedural generation** for obstacle placement
- **Clean separation** of rendering and game logic

---

## 🎮 Game States

1. **Idle State**: Bat bobs gently, waiting for player to press SPACE
2. **Playing State**: Full physics active, obstacles scrolling, scoring enabled
3. **Game Over State**: Collision detected, final score shown, press R to restart

---

## 🌈 Visual Design Philosophy

The game uses a cohesive **dark cave aesthetic**:
- Deep purples and blues create mysterious atmosphere
- Glowing yellow insects provide visual contrast and goals
- Subtle particle effects add depth without distraction
- Clean UI keeps focus on gameplay
- Smooth animations enhance the premium feel

---

## 🔮 Future Enhancement Ideas

- [ ] High score persistence (save to file)
- [ ] Difficulty progression (speed increases over time)
- [ ] Sound effects and background music
- [ ] Particle effects on collision
- [ ] Multiple bat skins
- [ ] Leaderboard system
- [ ] Power-ups (shield, slow-motion, magnet)
- [ ] Day/night cycle backgrounds

---

## 📝 License

This project is open source and available for educational purposes. Feel free to fork, modify, and learn from the code!

---

## 🙏 Acknowledgments

- Inspired by the classic **Flappy Bird** by Dong Nguyen
- Built with ☕ **Java Swing** for cross-platform compatibility
- Designed for players who love reflex-based arcade challenges

---

<div align="center">

**Made with ☕ and Java Swing**

*A cave adventure that tests your reflexes and rewards your precision*

⭐ **Star this repo if you enjoyed the game!** ⭐

</div>
