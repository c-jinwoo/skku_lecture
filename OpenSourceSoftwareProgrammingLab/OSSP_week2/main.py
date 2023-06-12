"""
    강의 : 오픈소스소프트웨어실습
    날짜 : 2023.03.09
    내용 : 2주차 과제
"""

from typing import List

def twoSum(nums: List[int], target: int) -> List[int]:
    ans = []

    for i in (0, len(nums) - 2):
        for j in (i + 1, len(nums) - 1):
            if nums[i] + nums[j] == target:
                ans.append(i)
                ans.append(j)
                break

        if len(ans) > 0:
            break

    return ans

