import requests
from bs4 import BeautifulSoup
import pandas as pd

# 保存数据到 Excel 文件的函数
def save_to_excel(data):
    df = pd.DataFrame(data)
    df.columns = ["name", "no"]
    writer = pd.ExcelWriter("data.xlsx")
    df.to_excel(writer, index=False)
    writer.save()

def process_string(long_string):
    name = []
    no = []

    while True:
        name_index = long_string.find('},"name":"')
        if name_index == -1:
            break
        name_start = name_index + 10
        name_end = long_string.find('"', name_start)
        if name_end == -1:
            break
        name_str = long_string[name_start:name_end]
        name.append(name_str)
        long_string = long_string[name_end:]

        no_index = long_string.find('user_no":"')
        if no_index == -1:
            break
        no_start = no_index + 10
        no_end = no_start + 10
        no_str = long_string[no_start:no_end]
        no.append(no_str)
        long_string = long_string[no_end:]

    result = [[name[i], no[i]] for i in range(min(len(name), len(no)))]
    return result

def get_course_ids():
    url = 'http://class.xjtu.edu.cn/api/users/325060/courses?no_cache=1683219565267&conditions={%22keyword%22:%22%22}&fields=id,name,course_code,department(id,name),grade(id,name),klass(id,name),course_type,cover,small_cover,start_date,end_date,is_started,is_closed,academic_year_id,semester_id,credit,compulsory,second_name,display_name,created_user(id,name),org(is_enterprise_or_organization),org_id,instructors(id,name,email,avatar_small_url),public_scope,course_attributes(teaching_class_name,copy_status),audit_status,audit_remark,can_withdraw_course,imported_from,instructor_assistants(id),allow_clone,team_teachings(id,name,email)&page=1&page_size=100'
    headers = {
        # "Referer":"http://class.xjtu.edu.cn/course/11947/enrollments",
        "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6",
        "Host": "class.xjtu.edu.cn",
        "Cookie": "STATE=Y1AeyYUPmQsbeWqa1rQRv-c9Avn-oinAfq9jG2OKGqg.xUn2B_Hn4Qs.TronClass; Oauth_Session=user_token_1c0d5349-6a07-49e7-a58c-e4eccb2667cd; _ga=GA1.3.819187840.1683211506; _gat=1; session=V2-1-19d2fe0c-ab94-45f5-9691-9a502c9145ab.MzI1MDYw.1683300553.MiwQOeEeNhSBSEdEMNa1U3mLVRA",
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.68"
    }
    name = []
    response = requests.get(url, headers=headers)
    soup = BeautifulSoup(response.content, 'html.parser')
    long_string = str(soup)
    while True:
        name_index = long_string.find('null,"id":')
        if name_index == -1:
            break
        name_start = name_index + 10
        name_end = name_start + 5
        name_str = long_string[name_start:name_end]
        name.append(name_str)
        long_string = long_string[name_end:]
    return name

# 爬取并解析网页内容的函数
def scrape_page(url):
    headers = {
        #"Referer":"http://class.xjtu.edu.cn/course/11947/enrollments",
        "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6",
        "Host":"class.xjtu.edu.cn",
        "Cookie":"_ga=GA1.3.819187840.1683211506; STATE=foCtoOetcftl0NPqZ2V20LqqYAna1dOvCnGa_Y2X0yM.WyYUsnRDt2k.TronClass; Oauth_Session=user_token_3a07fd31-1310-4aba-b0f8-6f47c4d3dd45; _gat=1; session=V2-1-f7fa54c1-8080-43a5-9c73-373871deb0b5.MzI1MzQy.1683309240.aJMuhZU2GF-rDDk5aY-1YuaAKuI",
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.68"
    }
    response = requests.get(url, headers=headers)
    soup = BeautifulSoup(response.text, "html.parser")
    if(str(soup)[:5]=='{"mes'):
        return False
    shuzu = process_string(str(soup))
    return shuzu

# 主函数
def main():
    base_url = "http://class.xjtu.edu.cn/api/course/{}/enrollments?fields=id,user(id,email,name,nickname,user_no,comment,grade(id,name),klass(id,name,code),department(id,name,code),org(id,name)),roles,retake_status,seat_number&no_cache=1683222981415"
    data = []
    urls = get_course_ids()
    for i in urls:
        url = base_url.format(i)
        print("正在爬取第{}页".format(i))
        page_data = scrape_page(url)
        if(page_data==False):
            print("爬取第{}页失败".format(i))
            continue
        else:
            print("爬取第{}页成功: {}".format(i, url))
        data.extend(page_data)
    newdata =[]
    for i in data:
        s=0
        for j in newdata:
            if(i==j):
                s=1
                break
        if(s==0):
            newdata.append(i)
    save_to_excel(newdata)
    print("数据保存完成！")

if __name__ == '__main__':
    main()
