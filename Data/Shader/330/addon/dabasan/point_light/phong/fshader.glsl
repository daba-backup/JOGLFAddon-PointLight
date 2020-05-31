#version 330

struct Camera{
    vec3 position;
    vec3 target;
    mat4 projection;
    mat4 view_transformation;
    float near;
    float far;
};
struct PointLight{
    bool enabled;

    vec3 position;
    float k0;
    float k1;
    float k2;
    vec4 diffuse_color;
    float diffuse_power;

    float color_clamp_min;
    float color_clamp_max;
};

const int MAX_POINT_LIGHT_NUM=256;

uniform Camera camera;
uniform sampler2D texture_sampler;
uniform PointLight lights[MAX_POINT_LIGHT_NUM];
uniform int current_point_light_num;
uniform float point_light_color_sum_clamp_min;
uniform float point_light_color_sum_clamp_max;

in vec3 vs_out_position;
in vec2 vs_out_uv;
in vec3 vs_out_normal;
out vec4 fs_out_color;

void main(){
    int bound=min(current_point_light_num,MAX_POINT_LIGHT_NUM);
    vec4 point_light_color_sum=vec4(0.0,0.0,0.0,1.0);
    for(int i=0;i<bound;i++){
        if(lights[i].enabled==false){
            continue;
        }

        vec3 r=vs_out_position-lights[i].position;
        float length_r=length(r);
        float attenuation=1.0/(lights[i].k0+lights[i].k1*pow(length_r,1.0)+lights[i].k2*pow(length_r,2.0));

        vec4 point_light_color=vec4(lights[i].diffuse_color*attenuation*lights[i].diffuse_power);
        point_light_color=clamp(point_light_color,lights[i].color_clamp_min,lights[i].color_clamp_max);

        point_light_color_sum+=point_light_color;
    }
    point_light_color_sum=clamp(point_light_color_sum,point_light_color_sum_clamp_min,point_light_color_sum_clamp_max);
    vec4 post_lighting_color=point_light_color_sum*texture(texture_sampler,vs_out_uv);

    fs_out_color=post_lighting_color;
    fs_out_color.a=1.0;
}
