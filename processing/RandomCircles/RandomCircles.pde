void setup() {
  size(800, 600);
  noStroke();
}

void draw() {
  fill(random(255), 50);
  float d = random(200);
  ellipse(random(width), random(height), d, d);
}