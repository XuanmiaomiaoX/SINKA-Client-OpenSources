#ifdef GL_ES
precision mediump float;
#endif

#extension GL_OES_standard_derivatives : enable
uniform float iTime;
uniform vec2 iResolution;

void main() {
    gl_FragColor = vec4(0.2,0.2,0.2,1.0);
}