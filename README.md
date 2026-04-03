<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bat Escape - Navigate the Dark Cave</title>
    <link href="https://fonts.googleapis.com/css2?family=Creepster&family=Nosifer&family=Special+Elite&family=Courier+Prime:wght@400;700&display=swap" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        :root {
            --cave-deep: #0a0a14;
            --cave-dark: #151520;
            --cave-mid: #1f1f30;
            --cave-light: #2a2a40;
            --rock-gray: #3a3a50;
            --bat-purple: #7878a0;
            --insect-glow: #ffd25a;
            --insect-bright: #ffe690;
            --text-light: #e6e6f0;
            --text-dim: #bebece;
            --danger-red: #ff7878;
        }

        body {
            font-family: 'Courier Prime', monospace;
            background: linear-gradient(180deg, var(--cave-deep) 0%, var(--cave-dark) 50%, var(--cave-mid) 100%);
            color: var(--text-light);
            line-height: 1.6;
            overflow-x: hidden;
            position: relative;
        }

        /* Cave atmosphere - animated dots */
        body::before {
            content: '';
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-image: 
                radial-gradient(1px 1px at 20% 30%, rgba(255,255,255,0.1), transparent),
                radial-gradient(1px 1px at 60% 70%, rgba(255,255,255,0.08), transparent),
                radial-gradient(2px 2px at 50% 50%, rgba(255,255,255,0.06), transparent),
                radial-gradient(1px 1px at 80% 10%, rgba(255,255,255,0.09), transparent),
                radial-gradient(1px 1px at 90% 60%, rgba(255,255,255,0.07), transparent);
            background-size: 200px 200px, 300px 300px, 250px 250px, 400px 400px, 350px 350px;
            background-position: 0 0, 40px 60px, 130px 270px, 70px 100px, 200px 150px;
            animation: caveDrift 60s linear infinite;
            pointer-events: none;
            z-index: 0;
        }

        @keyframes caveDrift {
            0% { transform: translateY(0); }
            100% { transform: translateY(200px); }
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
            position: relative;
            z-index: 1;
        }

        /* Hero Section */
        .hero {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            text-align: center;
            position: relative;
            overflow: hidden;
        }

        .title {
            font-family: 'Nosifer', cursive;
            font-size: clamp(3rem, 10vw, 7rem);
            color: var(--bat-purple);
            text-shadow: 
                0 0 20px rgba(120, 120, 160, 0.5),
                0 0 40px rgba(120, 120, 160, 0.3),
                0 4px 8px rgba(0, 0, 0, 0.8);
            margin-bottom: 1rem;
            letter-spacing: 0.1em;
            animation: titleGlow 3s ease-in-out infinite;
        }

        @keyframes titleGlow {
            0%, 100% { 
                text-shadow: 
                    0 0 20px rgba(120, 120, 160, 0.5),
                    0 0 40px rgba(120, 120, 160, 0.3),
                    0 4px 8px rgba(0, 0, 0, 0.8);
            }
            50% { 
                text-shadow: 
                    0 0 30px rgba(120, 120, 160, 0.7),
                    0 0 60px rgba(120, 120, 160, 0.5),
                    0 4px 8px rgba(0, 0, 0, 0.8);
            }
        }

        .subtitle {
            font-family: 'Special Elite', cursive;
            font-size: clamp(1rem, 3vw, 1.5rem);
            color: var(--text-dim);
            margin-bottom: 3rem;
            letter-spacing: 0.05em;
        }

        /* Interactive Game Demo */
        .game-demo {
            width: 100%;
            max-width: 900px;
            height: 400px;
            background: linear-gradient(180deg, var(--cave-deep) 0%, var(--cave-dark) 100%);
            border: 3px solid var(--rock-gray);
            border-radius: 12px;
            position: relative;
            overflow: hidden;
            margin: 3rem auto;
            box-shadow: 
                0 20px 60px rgba(0, 0, 0, 0.8),
                inset 0 0 100px rgba(0, 0, 0, 0.5);
        }

        .demo-floor {
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            height: 20px;
            background: var(--rock-gray);
            border-top: 2px solid var(--cave-light);
        }

        .bat {
            position: absolute;
            left: 180px;
            top: 50%;
            transform: translateY(-50%);
            width: 42px;
            height: 28px;
            animation: batFloat 2s ease-in-out infinite;
            z-index: 10;
        }

        @keyframes batFloat {
            0%, 100% { transform: translateY(-50%) rotate(-3deg); }
            50% { transform: translateY(calc(-50% - 15px)) rotate(3deg); }
        }

        .bat-body {
            width: 100%;
            height: 100%;
            background: var(--bat-purple);
            border-radius: 50%;
            position: relative;
            box-shadow: 0 0 15px rgba(120, 120, 160, 0.4);
        }

        .bat-wing {
            position: absolute;
            left: -14px;
            top: 8px;
            width: 0;
            height: 0;
            border-left: 22px solid #5a5a82;
            border-top: 6px solid transparent;
            border-bottom: 6px solid transparent;
            animation: wingFlap 0.4s ease-in-out infinite;
            transform-origin: right center;
        }

        @keyframes wingFlap {
            0%, 100% { transform: scaleX(1) translateY(0); }
            50% { transform: scaleX(0.6) translateY(-4px); }
        }

        .bat-eye {
            position: absolute;
            right: 10px;
            top: 7px;
            width: 7px;
            height: 7px;
            background: var(--text-light);
            border-radius: 50%;
        }

        .bat-eye::after {
            content: '';
            position: absolute;
            right: 2px;
            top: 2px;
            width: 3px;
            height: 3px;
            background: var(--cave-deep);
            border-radius: 50%;
        }

        .obstacle {
            position: absolute;
            width: 70px;
            background: var(--rock-gray);
            box-shadow: inset -10px 0 20px rgba(0, 0, 0, 0.5);
            animation: obstacleMove 4s linear infinite;
        }

        @keyframes obstacleMove {
            0% { transform: translateX(900px); }
            100% { transform: translateX(-70px); }
        }

        .obstacle-top {
            top: 0;
            height: 120px;
            border-bottom: 3px solid var(--cave-light);
        }

        .obstacle-bottom {
            bottom: 20px;
            height: 140px;
            border-top: 3px solid var(--cave-light);
        }

        .obstacle-1 { animation-delay: 0s; }
        .obstacle-2 { animation-delay: 1.3s; }
        .obstacle-3 { animation-delay: 2.6s; }

        /* Spikes on obstacles */
        .obstacle::before {
            content: '';
            position: absolute;
            left: -12px;
            top: 0;
            height: 100%;
            width: 12px;
            background: 
                linear-gradient(135deg, transparent 48%, var(--cave-light) 50%, transparent 52%) 0 0,
                linear-gradient(135deg, transparent 48%, var(--cave-light) 50%, transparent 52%) 0 15px,
                linear-gradient(135deg, transparent 48%, var(--cave-light) 50%, transparent 52%) 0 30px,
                linear-gradient(135deg, transparent 48%, var(--cave-light) 50%, transparent 52%) 0 45px;
            background-size: 12px 15px;
            background-repeat: repeat-y;
        }

        .insect {
            position: absolute;
            width: 18px;
            height: 18px;
            top: 50%;
            background: var(--insect-glow);
            border-radius: 50%;
            box-shadow: 
                0 0 20px var(--insect-bright),
                0 0 40px rgba(255, 210, 90, 0.5);
            animation: insectFloat 2s ease-in-out infinite, insectMove 4s linear infinite;
        }

        @keyframes insectFloat {
            0%, 100% { transform: translateY(-50%) scale(1); }
            50% { transform: translateY(-50%) scale(1.2); }
        }

        @keyframes insectMove {
            0% { left: 900px; }
            100% { left: -20px; }
        }

        .insect-1 { animation-delay: 0.5s, 0.5s; }
        .insect-2 { animation-delay: 1.8s, 1.8s; top: 35%; }

        .insect::before,
        .insect::after {
            content: '';
            position: absolute;
            width: 10px;
            height: 8px;
            background: rgba(240, 240, 255, 0.3);
            border-radius: 50%;
            top: 2px;
        }

        .insect::before {
            left: -8px;
        }

        .insect::after {
            right: -8px;
        }

        /* Sections */
        section {
            padding: 5rem 0;
            position: relative;
        }

        h2 {
            font-family: 'Creepster', cursive;
            font-size: clamp(2.5rem, 6vw, 4rem);
            color: var(--bat-purple);
            text-align: center;
            margin-bottom: 3rem;
            text-shadow: 0 0 20px rgba(120, 120, 160, 0.3);
            letter-spacing: 0.05em;
        }

        h3 {
            font-family: 'Special Elite', cursive;
            font-size: 1.5rem;
            color: var(--insect-bright);
            margin-bottom: 1rem;
        }

        /* Features Grid */
        .features {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 2rem;
            margin-top: 3rem;
        }

        .feature-card {
            background: linear-gradient(135deg, var(--cave-dark) 0%, var(--cave-mid) 100%);
            padding: 2rem;
            border-radius: 12px;
            border: 2px solid var(--rock-gray);
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
        }

        .feature-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: linear-gradient(135deg, transparent 0%, rgba(120, 120, 160, 0.1) 100%);
            opacity: 0;
            transition: opacity 0.3s ease;
        }

        .feature-card:hover {
            transform: translateY(-5px);
            border-color: var(--bat-purple);
            box-shadow: 
                0 10px 30px rgba(0, 0, 0, 0.6),
                0 0 20px rgba(120, 120, 160, 0.2);
        }

        .feature-card:hover::before {
            opacity: 1;
        }

        .feature-icon {
            font-size: 3rem;
            margin-bottom: 1rem;
            display: inline-block;
            animation: iconBob 3s ease-in-out infinite;
        }

        .feature-card:nth-child(1) .feature-icon { animation-delay: 0s; }
        .feature-card:nth-child(2) .feature-icon { animation-delay: 0.5s; }
        .feature-card:nth-child(3) .feature-icon { animation-delay: 1s; }
        .feature-card:nth-child(4) .feature-icon { animation-delay: 1.5s; }

        @keyframes iconBob {
            0%, 100% { transform: translateY(0) rotate(0deg); }
            50% { transform: translateY(-10px) rotate(5deg); }
        }

        .feature-card p {
            color: var(--text-dim);
            line-height: 1.8;
            position: relative;
            z-index: 1;
        }

        /* How to Play */
        .controls {
            background: var(--cave-dark);
            padding: 3rem;
            border-radius: 12px;
            border: 2px solid var(--rock-gray);
            margin: 3rem 0;
            max-width: 800px;
            margin-left: auto;
            margin-right: auto;
        }

        .control-item {
            display: flex;
            align-items: center;
            gap: 2rem;
            margin-bottom: 2rem;
            padding: 1.5rem;
            background: var(--cave-mid);
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .control-item:hover {
            background: var(--cave-light);
            transform: translateX(10px);
        }

        .key {
            font-family: 'Courier Prime', monospace;
            font-weight: bold;
            background: var(--rock-gray);
            color: var(--text-light);
            padding: 1rem 1.5rem;
            border-radius: 8px;
            border: 2px solid var(--cave-light);
            box-shadow: 
                0 4px 0 var(--cave-deep),
                0 6px 10px rgba(0, 0, 0, 0.5);
            min-width: 120px;
            text-align: center;
            font-size: 1.2rem;
        }

        /* Installation */
        .code-block {
            background: var(--cave-deep);
            padding: 2rem;
            border-radius: 8px;
            border: 2px solid var(--rock-gray);
            font-family: 'Courier Prime', monospace;
            color: var(--insect-bright);
            overflow-x: auto;
            margin: 2rem 0;
            position: relative;
        }

        .code-block::before {
            content: '>';
            position: absolute;
            left: 1rem;
            color: var(--bat-purple);
            animation: cursorBlink 1s infinite;
        }

        @keyframes cursorBlink {
            0%, 50% { opacity: 1; }
            51%, 100% { opacity: 0; }
        }

        .code-block code {
            padding-left: 1.5rem;
            display: block;
        }

        /* Stats Bar */
        .stats {
            display: flex;
            justify-content: center;
            gap: 3rem;
            flex-wrap: wrap;
            margin: 3rem 0;
        }

        .stat {
            text-align: center;
        }

        .stat-number {
            font-family: 'Special Elite', cursive;
            font-size: 3rem;
            color: var(--insect-bright);
            text-shadow: 0 0 20px rgba(255, 210, 90, 0.5);
            display: block;
        }

        .stat-label {
            color: var(--text-dim);
            font-size: 0.9rem;
            text-transform: uppercase;
            letter-spacing: 0.1em;
        }

        /* Footer */
        footer {
            text-align: center;
            padding: 3rem 0;
            border-top: 2px solid var(--rock-gray);
            margin-top: 5rem;
        }

        footer a {
            color: var(--bat-purple);
            text-decoration: none;
            transition: all 0.3s ease;
        }

        footer a:hover {
            color: var(--insect-bright);
            text-shadow: 0 0 10px rgba(255, 210, 90, 0.5);
        }

        /* Responsive */
        @media (max-width: 768px) {
            .hero {
                min-height: 80vh;
            }

            .game-demo {
                height: 300px;
            }

            .features {
                grid-template-columns: 1fr;
            }

            .control-item {
                flex-direction: column;
                text-align: center;
            }

            .stats {
                gap: 2rem;
            }
        }

        /* Scroll fade-in animation */
        .fade-in {
            opacity: 0;
            transform: translateY(30px);
            animation: fadeInUp 0.8s ease forwards;
        }

        @keyframes fadeInUp {
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .fade-in:nth-child(1) { animation-delay: 0.1s; }
        .fade-in:nth-child(2) { animation-delay: 0.2s; }
        .fade-in:nth-child(3) { animation-delay: 0.3s; }
        .fade-in:nth-child(4) { animation-delay: 0.4s; }
    </style>
</head>
<body>
    <div class="container">
        <!-- Hero Section -->
        <header class="hero">
            <h1 class="title">BAT ESCAPE</h1>
            <p class="subtitle">Navigate the Deadly Cave • Collect Glowing Insects • Survive</p>
            
            <!-- Interactive Game Demo -->
            <div class="game-demo">
                <div class="bat">
                    <div class="bat-body">
                        <div class="bat-wing"></div>
                        <div class="bat-eye"></div>
                    </div>
                </div>
                
                <div class="obstacle obstacle-top obstacle-1"></div>
                <div class="obstacle obstacle-bottom obstacle-1"></div>
                <div class="obstacle obstacle-top obstacle-2"></div>
                <div class="obstacle obstacle-bottom obstacle-2"></div>
                <div class="obstacle obstacle-top obstacle-3"></div>
                <div class="obstacle obstacle-bottom obstacle-3"></div>
                
                <div class="insect insect-1"></div>
                <div class="insect insect-2"></div>
                
                <div class="demo-floor"></div>
            </div>
        </header>

        <!-- Stats Section -->
        <div class="stats">
            <div class="stat">
                <span class="stat-number">∞</span>
                <span class="stat-label">Endless Gameplay</span>
            </div>
            <div class="stat">
                <span class="stat-number">+50</span>
                <span class="stat-label">Bonus Per Insect</span>
            </div>
            <div class="stat">
                <span class="stat-number">60</span>
                <span class="stat-label">FPS Smooth Action</span>
            </div>
        </div>

        <!-- Features Section -->
        <section>
            <h2>Game Features</h2>
            <div class="features">
                <div class="feature-card fade-in">
                    <div class="feature-icon">🦇</div>
                    <h3>Flappy Bird Mechanics</h3>
                    <p>Master the art of flight! Press or hold SPACE to make your bat flap through the treacherous cave. Precise timing is your key to survival.</p>
                </div>
                <div class="feature-card fade-in">
                    <div class="feature-icon">🪨</div>
                    <h3>Dynamic Obstacles</h3>
                    <p>Navigate through randomly generated cave formations with spiked stalactites and stalagmites. Every game is a unique challenge!</p>
                </div>
                <div class="feature-card fade-in">
                    <div class="feature-icon">✨</div>
                    <h3>Bonus Collectibles</h3>
                    <p>Catch glowing insects for massive bonus points! Risk versus reward gameplay adds strategic depth to every flight.</p>
                </div>
                <div class="feature-card fade-in">
                    <div class="feature-icon">🎮</div>
                    <h3>Smooth Java Swing</h3>
                    <p>Built with Java Swing for silky-smooth 60 FPS gameplay. Responsive controls and polished animations deliver premium feel.</p>
                </div>
            </div>
        </section>

        <!-- How to Play -->
        <section>
            <h2>How to Play</h2>
            <div class="controls">
                <div class="control-item">
                    <div class="key">SPACE</div>
                    <div>
                        <h3>Flap / Start Game</h3>
                        <p>Press or hold SPACE to make the bat flap upward. Release to let gravity pull you down. Master the rhythm!</p>
                    </div>
                </div>
                <div class="control-item">
                    <div class="key">R</div>
                    <div>
                        <h3>Restart</h3>
                        <p>Hit an obstacle? Press R to restart and try again. Every attempt makes you better!</p>
                    </div>
                </div>
                <div class="control-item">
                    <div class="key">ESC</div>
                    <div>
                        <h3>Exit Game</h3>
                        <p>Press ESC to exit the game anytime. Your scores aren't saved, so make each run count!</p>
                    </div>
                </div>
            </div>
        </section>

        <!-- Installation -->
        <section>
            <h2>Installation & Running</h2>
            
            <h3 style="color: var(--text-light); text-align: left; max-width: 800px; margin: 2rem auto 1rem;">Prerequisites</h3>
            <div class="code-block">
                <code>Java Development Kit (JDK) 8 or higher</code>
            </div>

            <h3 style="color: var(--text-light); text-align: left; max-width: 800px; margin: 2rem auto 1rem;">Compile & Run</h3>
            <div class="code-block">
                <code>javac BatEscape.java</code>
            </div>
            <div class="code-block">
                <code>java BatEscape</code>
            </div>

            <h3 style="color: var(--text-light); text-align: left; max-width: 800px; margin: 2rem auto 1rem;">Quick Start (One Command)</h3>
            <div class="code-block">
                <code>javac BatEscape.java && java BatEscape</code>
            </div>
        </section>

        <!-- Technical Details -->
        <section>
            <h2>Technical Highlights</h2>
            <div class="features">
                <div class="feature-card">
                    <h3>🎨 Custom Graphics</h3>
                    <p>Hand-coded 2D graphics using Java2D. Dark gradient backgrounds, glowing effects, and smooth animations create an immersive cave atmosphere.</p>
                </div>
                <div class="feature-card">
                    <h3>⚡ Real-time Physics</h3>
                    <p>Gravity simulation with velocity damping. Responsive flap mechanics that feel natural and rewarding to master.</p>
                </div>
                <div class="feature-card">
                    <h3>🔄 Object Recycling</h3>
                    <p>Efficient obstacle recycling system prevents memory bloat during extended gameplay sessions. Performance optimized for endless runs.</p>
                </div>
                <div class="feature-card">
                    <h3>🎯 Collision Detection</h3>
                    <p>Precise rectangle-based collision detection ensures fair and predictable gameplay. You always know exactly why you died!</p>
                </div>
            </div>
        </section>

        <!-- Footer -->
        <footer>
            <p>Made with ☕ and Java Swing</p>
            <p style="margin-top: 1rem; color: var(--text-dim);">
                A Flappy Bird-inspired cave adventure | Open Source
            </p>
        </footer>
    </div>

    <script>
        // Intersection Observer for scroll animations
        const observerOptions = {
            threshold: 0.1,
            rootMargin: '0px 0px -100px 0px'
        };

        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.style.animationPlayState = 'running';
                }
            });
        }, observerOptions);

        document.querySelectorAll('.fade-in').forEach(el => {
            observer.observe(el);
        });
    </script>
</body>
</html>
